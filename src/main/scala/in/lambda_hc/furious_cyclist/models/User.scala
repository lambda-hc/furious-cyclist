package in.lambda_hc.furious_cyclist.models

import java.sql.ResultSet
import in.lambda_hc.furious_cyclist.connectors.MysqlClient
import in.lambda_hc.furious_cyclist.utils.SecurityUtils
import org.slf4j.LoggerFactory
import spray.json.{JsString, JsNumber, JsObject}

/**
  * Created by vishnu on 10/6/16.
  */
class User(
            val userId: Long = 0,
            val userName: String,
            val passwordHash: String,
            val name: String,
            val email: String,
            val city: String
          ) {

  def toJson: JsObject = JsObject(
    "id" -> JsNumber(userId),
    "name" -> JsString(name),
    "email" -> JsString(email),
    "userName" -> JsString(userName)
  )

}


object User {

  val LOG = LoggerFactory.getLogger(this.getClass)

  private def getUser(rs: ResultSet): User = {
    new User(
      userId = rs.getLong("userId"),
      userName = rs.getString("username"),
      passwordHash = rs.getString("password_hash"),
      name = rs.getString("name"),
      email = rs.getString("email"),
      city = rs.getString("city")
    )
  }

  def getUser(id: Long): User = {
    val rs = MysqlClient.getResultSet("select * from users where userId=" + id)

    if (rs.next())
      getUser(rs)
    else
      null
  }

  def authenticateAndGetUser(emailOrUsername: String, password: String): User = {

    val rs = MysqlClient.getResultSet("select * from users where ( email='" + emailOrUsername + "' || userName='" + emailOrUsername + "') && password_hash='" + SecurityUtils.hash(password) + "'")

    val result = if (rs.next())
      getUser(rs)
    else null

    rs.close()

    result
  }

  def getUser(userJson: JsObject): User = {
    try {

      val userId = if (userJson.getFields("userId").nonEmpty)
        userJson.getFields("userId").head.asInstanceOf[JsNumber].value.toLong
      else 0

      val userName = if (userJson.getFields("userName").nonEmpty)
        userJson.getFields("userName").head.asInstanceOf[JsString].value
      else ""

      val passwordHash = if (userJson.getFields("password").nonEmpty)
        SecurityUtils.hash(userJson.getFields("password").head.asInstanceOf[JsString].value)
      else ""

      val name = if (userJson.getFields("name").nonEmpty)
        userJson.getFields("name").head.asInstanceOf[JsString].value
      else ""

      val email = if (userJson.getFields("email").nonEmpty)
        userJson.getFields("email").head.asInstanceOf[JsString].value
      else ""

      val city = if (userJson.getFields("city").nonEmpty)
        userJson.getFields("city").head.asInstanceOf[JsString].value
      else ""

      if (userName.nonEmpty && name.nonEmpty && email.nonEmpty)
        new User(
          userId = userId,
          userName = userName,
          passwordHash = passwordHash,
          name = name,
          email = email,
          city = city
        )
      else
        null
    }

    catch {
      case e: Exception => null
    }
  }

  def toDB(user: User) = {
    if (user.userId == 0)
      MysqlClient.insert(
        tableName = "users",
        elements = Map(
          "username" -> user.userName,
          "password_hash" -> user.passwordHash,
          "name" -> user.name,
          "email" -> user.email,
          "city" -> user.city
        )
      )

    else
      MysqlClient.update(
        tableName = "users",
        elements = Map(
          "username" -> user.userName,
          "password_hash" -> user.passwordHash,
          "name" -> user.name,
          "email" -> user.email,
          "city" -> user.city
        ),
        keyFieldName = "userId",
        keyValue = user.userId
      )
  }

}
