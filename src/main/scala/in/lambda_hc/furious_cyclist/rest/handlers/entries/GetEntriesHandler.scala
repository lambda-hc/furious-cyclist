package in.lambda_hc.furious_cyclist.rest.handlers.entries

import in.lambda_hc.furious_cyclist.connectors.MysqlClient
import in.lambda_hc.furious_cyclist.models.EntryModel
import in.lambda_hc.furious_cyclist.utils.UNDERTOW_HELPERS
import io.undertow.server.{HttpServerExchange, HttpHandler}
import spray.json.{JsArray, JsString, JsObject}
import collection.JavaConversions._

/**
  * Created by vishnu on 18/6/16.
  */
class GetEntriesHandler extends HttpHandler {
  override def handleRequest(exchange: HttpServerExchange): Unit = {
    exchange.getResponseHeaders
      .add(UNDERTOW_HELPERS.ACCESS_CONTROL_ALLOW_HEADERS._1, UNDERTOW_HELPERS.ACCESS_CONTROL_ALLOW_HEADERS._2)
      .add(UNDERTOW_HELPERS.ACCESS_CONTROL_ALLOW_CREDENTIALS._1, UNDERTOW_HELPERS.ACCESS_CONTROL_ALLOW_CREDENTIALS._2)
      .add(UNDERTOW_HELPERS.ACCESS_CONTROL_ALLOW_METHODS._1, UNDERTOW_HELPERS.ACCESS_CONTROL_ALLOW_METHODS._2)
      .add(UNDERTOW_HELPERS.ACCESS_CONTROL_MAX_AGE._1, UNDERTOW_HELPERS.ACCESS_CONTROL_MAX_AGE._2)

    val queryParams = exchange.getQueryParameters.map(i => {
      (i._1, if (i._2.nonEmpty) i._2.getFirst else null)
    }).toMap

    val whereQuery = Array(
      {
        val v = queryParams.getOrElse("fromUser", null)
        if (v != null) "userId=" + v else null
      }, {
        val v = queryParams.getOrElse("startDate", null)
        if (v != null) "registeredDate>='" + v + "' " else null
      }, {
        val v = queryParams.getOrElse("endDate", null)
        if (v != null) "registeredDate<='" + v + "' " else null
      }, {
        val v = queryParams.getOrElse("vehicle", null)
        if (v != null) "vehicle=" + v else null
      }, {
        val v = queryParams.getOrElse("city", null)
        if (v != null) "city='" + v + "'" else null
      }
    ).filter(_ != null).mkString(" AND ")

    val rs = MysqlClient.getResultSet("select * from entries " + (if (whereQuery.nonEmpty) "where " + whereQuery else ""))

    val buf = scala.collection.mutable.ListBuffer.empty[JsObject]


    while (rs.next()) {
      buf += EntryModel.getFromResultSet(rs).toJson
    }

    exchange.getResponseSender.send(
      JsObject(
        "status" -> JsString("ok"),
        "entries" -> JsArray(buf.toVector)
      ).prettyPrint
    )

  }
}
