package in.lambda_hc.furious_cyclist.rest.handlers.entries

import io.undertow.server.{HttpServerExchange, HttpHandler}
import in.lambda_hc.furious_cyclist.ServerBootstrap.mysqlClient
import collection.JavaConversions._

/**
  * Created by vishnu on 18/6/16.
  */
class GetEntriesHandler extends HttpHandler {
  override def handleRequest(exchange: HttpServerExchange): Unit = {
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
        if (v != null) "city=" + v else null
      }
    ).filter(_ != null).mkString(" AND ")

    mysqlClient.getResultSet("select * from entries " + (if (whereQuery.nonEmpty) whereQuery else ""))



  }
}
