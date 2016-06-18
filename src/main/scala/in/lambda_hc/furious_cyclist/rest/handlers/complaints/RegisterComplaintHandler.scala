package in.lambda_hc.furious_cyclist.rest.handlers.complaints

import in.lambda_hc.furious_cyclist.models.EntryModel
import io.undertow.server.{HttpServerExchange, HttpHandler}
import in.lambda_hc.furious_cyclist.ServerBootstrap.sessionHandler
import org.apache.commons.io.IOUtils
import spray.json.{JsArray, JsString, JsObject}
import spray.json._

/**
  * Created by vishnu on 18/6/16.
  */
class RegisterComplaintHandler extends HttpHandler {
  override def handleRequest(exchange: HttpServerExchange): Unit = {
    val cookie = exchange.getRequestCookies.get("ssid")

    val user = if (cookie != null)
      sessionHandler.getUserForSession(cookie.getValue)
    else null

    if (user != null) {
      if (exchange.isInIoThread) {
        exchange.dispatch(this)
      } else {
        exchange.startBlocking()
        val request = new String(IOUtils.toByteArray(exchange.getInputStream))

        val requestJson = request.parseJson.asJsObject

        val vehicleNumber = if (requestJson.getFields("vehicleNumber").nonEmpty) requestJson.getFields("vehicleNumber").head.asInstanceOf[JsString].value else ""
        val description = if (requestJson.getFields("description").nonEmpty) requestJson.getFields("description").head.asInstanceOf[JsString].value else ""
        val location = if (requestJson.getFields("location").nonEmpty) requestJson.getFields("location").head.asInstanceOf[JsString].value else ""
        val city = if (requestJson.getFields("city").nonEmpty) requestJson.getFields("city").head.asInstanceOf[JsString].value else ""

        var entry = new EntryModel(
          userId = user.userId,
          vehicleNumber = vehicleNumber,
          description = description,
          location = location,
          city = city
        )

        entry = EntryModel.saveToDb(entry)

        if (entry != null)
          exchange.getResponseSender.send(JsObject(
            "status" -> JsString("ok"),
            "entry" -> entry.toJson
          ).prettyPrint)
        else
          exchange.getResponseSender.send(JsObject(
            "status" -> JsString("failed")
          ).prettyPrint)
      }
    } else {
      exchange.getResponseSender.send(
        JsObject(
          "status" -> JsString("failed"),
          "message" -> JsArray(JsString("user Not Authenticated"))
        ).prettyPrint)
    }
  }

}
