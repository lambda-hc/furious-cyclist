package in.lambda_hc.furious_cyclist.rest.undertow.handlers.auth

import com.google.inject.Inject
import in.lambda_hc.furious_cyclist.rest.controllers.UserController
import in.lambda_hc.furious_cyclist.rest.controllers.session.SessionHandler

import in.lambda_hc.furious_cyclist.utils.{UNDERTOW_HELPERS, SecurityUtils}
import io.undertow.server.{HttpHandler, HttpServerExchange}
import io.undertow.util.HttpString
import org.apache.commons.io.IOUtils
import spray.json.JsonParser.ParsingException
import spray.json.{JsArray, JsObject, JsString, _}

/**
  * Created by vishnu on 12/6/16.
  */

class LoginHandler @Inject()(sessionHandler: SessionHandler) extends HttpHandler {
  override def handleRequest(exchange: HttpServerExchange): Unit = {
    if (!exchange.getResponseHeaders.contains("Access-Control-Allow-Origin")) {
      exchange.getResponseHeaders.add(new HttpString("Access-Control-Allow-Origin"), "*");
    }
    exchange.getResponseHeaders
      .add(UNDERTOW_HELPERS.ACCESS_CONTROL_ALLOW_HEADERS._1, UNDERTOW_HELPERS.ACCESS_CONTROL_ALLOW_HEADERS._2)
      .add(UNDERTOW_HELPERS.ACCESS_CONTROL_ALLOW_CREDENTIALS._1, UNDERTOW_HELPERS.ACCESS_CONTROL_ALLOW_CREDENTIALS._2)
      .add(UNDERTOW_HELPERS.ACCESS_CONTROL_ALLOW_METHODS._1, UNDERTOW_HELPERS.ACCESS_CONTROL_ALLOW_METHODS._2)
      .add(UNDERTOW_HELPERS.ACCESS_CONTROL_MAX_AGE._1, UNDERTOW_HELPERS.ACCESS_CONTROL_MAX_AGE._2)
    val cookie = exchange.getRequestCookies.get("ssid")

    val user = if (cookie != null)
      sessionHandler.getUserForSession(cookie.getValue)
    else null

    if (user == null) {
      if (exchange.isInIoThread) {
        exchange.dispatch(this)
      } else {
        try {
          exchange.startBlocking()
          val request = new String(IOUtils.toByteArray(exchange.getInputStream))

          val requestJson = request.parseJson.asJsObject

          val (user, message) = UserController.authenticateUser(requestJson)

          if (user != null) {
            //TODO add logic for Successful Registration
            val token = sessionHandler.createSessionTokenForUser(user.userId)


            exchange.setResponseCookie(SecurityUtils.createCookie("ssid", token))

            exchange.getResponseSender.send(JsObject(
              "status" -> JsString("ok"),
              "message" -> JsString("Login successful"),
              "id_token" -> JsString(token),
              "user"->user.toJson
            ).prettyPrint)
          } else {
            //TODO add logic for Failed Registration
            exchange.setStatusCode(400)
            exchange.getResponseSender.send(JsObject(
              "status" -> JsString("failed"),
              "message" -> JsString("Login Failed"),
              "comments" -> JsArray(message.map(JsString(_)).toVector)
            ).prettyPrint)
          }


        } catch {
          case e: ParsingException => {
            e.printStackTrace()
            exchange.setStatusCode(200)
            exchange.getResponseSender.send(JsObject(
              "status" -> JsString("failed"),
              "message" -> JsString("Invalid Json Parsing Exception")
            ).prettyPrint)
          }
          case e: Exception => {
            e.printStackTrace()
            exchange.setStatusCode(200)
            exchange.getResponseSender.send(JsObject(
              "status" -> JsString("failed"),
              "message" -> JsString("Login Failed")
            ).prettyPrint)
          }
        }
      }
    } else {
      exchange.getResponseSender.send(JsObject(
        "status" -> JsString("ok"),
        "message" -> JsString("User is already logged in"),
        "user"->user.get.toJson
      ).prettyPrint)
    }
  }
}
