package in.lambda_hc.furious_cyclist.models

import java.sql.{Date, ResultSet}
import in.lambda_hc.furious_cyclist.ServerBootstrap.mysqlClient
import spray.json.{JsNumber, JsString, JsObject}

/**
  * Created by vishnu on 18/6/16.
  */
class EntryModel(
                  val entryId: Long = 0,
                  val userId: Long,
                  val vehicleNumber: String,
                  val description: String,
                  val location: String,
                  val city: String,
                  val registeredDate: Date = null
                ) {

  def toJson: JsObject = JsObject(
    "entryId" -> JsNumber(entryId),
    "userId" -> JsNumber(userId),
    "registeredDate" -> JsString(registeredDate.toString),
    "vehicleNumber" -> JsString(vehicleNumber),
    "description" -> JsString(description),
    "location" -> JsString(location),
    "city" -> JsString(city)
  )

}


object EntryModel {

  def get(entryId: Long): EntryModel = {
    val rs = mysqlClient.getResultSet("select * from entries where entryId=" + entryId)
    val response = if (rs.next())
      getFromResultSet(rs)
    else null
    rs.close()
    response
  }


  def getFromResultSet(rs: ResultSet): EntryModel = {
    new EntryModel(
      entryId = rs.getLong("entryId"),
      userId = rs.getLong("userId"),
      registeredDate = rs.getDate("registeredDate"),
      vehicleNumber = rs.getString("vehicleNumber"),
      description = rs.getString("description"),
      location = rs.getString("location"),
      city = rs.getString("city")
    )
  }

  def saveToDb(entryObj: EntryModel) = {
    if (entryObj.entryId == 0) {
      val complaintId = mysqlClient.insert(
        tableName = "entries",
        elements = Map(
          "userId" -> entryObj.userId,
          "vehicleNumber" -> entryObj.vehicleNumber,
          "description" -> entryObj.description,
          "location" -> entryObj.location,
          "city" -> entryObj.city
        )
      )
      get(complaintId)
    }
    else {
      mysqlClient.update(
        tableName = "entries",
        elements = Map(
          "userId" -> entryObj.userId,
          "vehicleNumber" -> entryObj.vehicleNumber,
          "description" -> entryObj.description,
          "location" -> entryObj.location,
          "city" -> entryObj.city
        ),
        keyFieldName = "entryId",
        keyValue = entryObj.entryId
      )
      get(entryObj.entryId)
    }
  }
}