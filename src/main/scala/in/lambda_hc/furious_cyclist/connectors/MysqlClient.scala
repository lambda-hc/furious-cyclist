package in.lambda_hc.furious_cyclist.connectors

import java.io.{ObjectInputStream, ByteArrayInputStream, ObjectOutputStream, ByteArrayOutputStream}
import java.sql.{PreparedStatement, ResultSet, DriverManager, Connection}

import com.google.inject.Inject
import com.typesafe.config.Config
import org.slf4j.{LoggerFactory, Logger}
import spray.json.{JsArray, JsObject}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by vishnu on 10/6/16.
  */
class MysqlClient @Inject()(config: Config) {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[MysqlClient])

  val host = config.getString("server.mysql.host")
  val port = config.getString("server.mysql.port")
  val user = config.getString("server.mysql.user")
  val password = config.getString("server.mysql.password")
  val db = config.getString("server.mysql.db")


  val autoIncValuesForTable: Map[String, Array[String]] = Map(
    "Users" -> Array("UserId")
  )

  private val dbc = "jdbc:mysql://" + host + ":" + port + "/" + db + "?user=" + user + "&password=" + password
  classOf[com.mysql.jdbc.Driver]
  private var conn: Connection = DriverManager.getConnection(dbc)

  /**
    * Returns connection object for MYSQL connection
    *
    * @return
    */
  def getConnection: Connection = {
    if (conn.isClosed) {
      conn = DriverManager.getConnection(dbc)
    }
    conn
  }

  def closeConnection() = conn.close()

  /**
    * Executes given query in mysql
    *
    * @param query
    * @return
    */
  def executeQuery(query: String): Boolean = {
    val statement = getConnection.createStatement()
    try
      statement.execute(query)
    finally statement.close()
  }

  /**
    * Get result set for given query.
    *
    * @param query
    * @return
    */
  def getResultSet(query: String): ResultSet = {
    LOG.debug("getResultSet Query : " + query)
    getConnection.createStatement().executeQuery(query)
  }

  /**
    * Insert data to the given table.
    *
    * @param tableName
    * @param elements
    * @return
    */
  def insert(tableName: String, elements: Map[String, Any]): Long = {
    try {
      val colNames: ArrayBuffer[String] = ArrayBuffer()
      val values: ArrayBuffer[Any] = ArrayBuffer()
      elements.foreach(i => {
        colNames += i._1
        values += i._2
      })

      val insertQuery = "INSERT INTO " + tableName + " (" + colNames.mkString(",") + ") VALUES (" + colNames.indices.map(i => "?").mkString(",") + ")"

      val returnColumns: Array[String] = autoIncValuesForTable.getOrElse(tableName, Array())
      val preparedStatement: PreparedStatement = getConnection.prepareStatement(insertQuery, returnColumns)

      values.zipWithIndex.foreach(i => addToPreparedStatement(i._1, i._2 + 1, preparedStatement))
      var generatedId: Long = 0l
      try {
        LOG.debug("insert Query : " + preparedStatement.toString)
        preparedStatement.executeUpdate()
        if (returnColumns.nonEmpty) {
          val gkSet = preparedStatement.getGeneratedKeys
          if (gkSet.next()) {
            generatedId = gkSet.getLong(1)
          }
        }
      }
      finally preparedStatement.close()

      generatedId
    } catch {
      case e: Exception => e.printStackTrace()
        0l
    }
  }

  /**
    * update records in the given table with provided values
    *
    * @param tableName    mysql table name
    * @param elements     elements to update
    * @param keyFieldName primary key field
    * @param keyValue     primary key value to update
    * @return
    */
  def update(tableName: String, elements: Map[String, Any], keyFieldName: String, keyValue: Any): Boolean = {
    try {
      val colNames: ArrayBuffer[String] = ArrayBuffer()
      val values: ArrayBuffer[Any] = ArrayBuffer()
      elements.foreach(i => {
        colNames += i._1
        values += i._2
      })

      val updateQuery = "UPDATE " + tableName + " SET " + colNames.map(_ + " = ? ").mkString(",") + " WHERE " + keyFieldName + " = ? ;"
      val preparedStatement: PreparedStatement = getConnection.prepareStatement(updateQuery)
      values.zipWithIndex.foreach(i => addToPreparedStatement(i._1, i._2 + 1, preparedStatement))

      addToPreparedStatement(keyValue, values.size + 1, preparedStatement)

      println("update statement " + preparedStatement)

      try {
        LOG.debug("update Query : " + preparedStatement.toString)
        preparedStatement.executeUpdate()
      }
      finally preparedStatement.close()

      true
    } catch {
      case e: Exception => e.printStackTrace()
        false
    }
  }

  /**
    * Delete record with given column name and colulmn value on a mysql table.
    *
    * @param tableName
    * @param columnName
    * @param value
    * @return
    */
  def deleteRecord(tableName: String, columnName: String, value: Any): Boolean = {
    try {
      val valueString: String = value match {
        case _: String => "'" + value.asInstanceOf[String] + "'"
        case Double => value.asInstanceOf[Double].toString
        case Long => value.asInstanceOf[Long].toString
        case _ => value.toString
      }
      deleteRecord(tableName, columnName + " = " + valueString)
    } catch {
      case e: Exception => e.printStackTrace()
        false
    }

  }

  /**
    * Delete record which satisfies given condition
    *
    * @param tableName
    * @param whereQuery
    * @return
    */
  def deleteRecord(tableName: String, whereQuery: String): Boolean = {
    try {
      val query: String = "DELETE FROM " + tableName + " WHERE " + whereQuery
      LOG.debug("DeleteRecord : " + query)
      executeQuery(query)
    } catch {
      case e: Exception =>
        false
    }

  }

  private def addToPreparedStatement(value: Any, index: Int, preparedStatement: PreparedStatement) = {
    value match {
      case v: Long => preparedStatement.setLong(index, v)
      case v: Int => preparedStatement.setInt(index, v)
      case v: Double => preparedStatement.setDouble(index, v)
      case v: String => preparedStatement.setString(index, v)
      case v: JsObject => preparedStatement.setString(index, v.toString())
      case v: JsArray => preparedStatement.setString(index, v.toString())
      case v: Array[Byte] => preparedStatement.setBytes(index, v)
      case v: Serializable => preparedStatement.setObject(index, v)
      case _ => preparedStatement.setString(index, value.toString)
    }
  }

  def serialize(v: Any): Array[Byte] = {
    val bos = new ByteArrayOutputStream
    val out: ObjectOutputStream = new ObjectOutputStream(bos)
    out.writeObject(v)
    out.close()
    val output = bos.toByteArray
    bos.close()
    output
  }

  def deSerialize(v: Array[Byte]): Any = {
    val bos = new ByteArrayInputStream(v)
    val out: ObjectInputStream = new ObjectInputStream(bos)
    val output = out.readObject()
    out.close()
    bos.close()
    output
  }

}
