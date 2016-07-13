package in.lambda_hc.furious_cyclist.rest.undertow.handlers.auth

import com.google.inject.Inject
import in.lambda_hc.furious_cyclist.rest.controllers.UserController
import in.lambda_hc.furious_cyclist.ServerBootstrap.sessionHandler
import in.lambda_hc.furious_cyclist.rest.controllers.session.SessionHandler
import in.lambda_hc.furious_cyclist.utils.UNDERTOW_HELPERS
import io.undertow.server.{HttpHandler, HttpServerExchange}
import org.apache.commons.io.IOUtils
import spray.json.JsonParser.ParsingException
import spray.json.{JsArray, JsObject, JsString, _}

/**
  * Created by vishnu on 11/6/16.
  */
//TODO make userController Singleton Remove DI
class RegisterHandler @Inject()(
                                 sessionHandler: SessionHandler
                               ) extends HttpHandler {

  override def handleRequest(exchange: HttpServerExchange): Unit = {
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

          val registrationTuple = UserController.registerUser(requestJson)

          if (registrationTuple._1 != null) {
            //TODO add logic for Successful Registration
            exchange.getResponseSender.send(JsObject(
              "status" -> JsString("ok"),
              "message" -> JsString("Registration successful")
            ).prettyPrint)
          } else {
            //TODO add logic for Failed Registration
            exchange.setStatusCode(400)
            exchange.getResponseSender.send(JsObject(
              "status" -> JsString("failed"),
              "message" -> JsString("Registration Failed"),
              "comments" -> JsArray(registrationTuple._2.map(JsString(_)).toVector)
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
              "message" -> JsString("Registration Failed")
            ).prettyPrint)
          }
        }
      }
    } else {
      exchange.getResponseSender.send(JsObject(
        "status" -> JsString("ok"),
        "message" -> JsString("User is already logged in")
      ).prettyPrint)
    }
  }

}
