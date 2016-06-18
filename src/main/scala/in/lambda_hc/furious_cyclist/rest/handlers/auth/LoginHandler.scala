package in.lambda_hc.furious_cyclist.rest.handlers.auth

import com.google.inject.Inject
import in.lambda_hc.furious_cyclist.rest.controllers.UserController
import in.lambda_hc.furious_cyclist.ServerBootstrap.sessionHandler
import in.lambda_hc.furious_cyclist.utils.SecurityUtils
import io.undertow.server.{HttpHandler, HttpServerExchange}
import org.apache.commons.io.IOUtils
import spray.json.JsonParser.ParsingException
import spray.json.{JsArray, JsObject, JsString, _}

/**
  * Created by vishnu on 12/6/16.
  */

//TODO make userController Singleton Remove DI
class LoginHandler @Inject()(userController: UserController) extends HttpHandler {
  override def handleRequest(exchange: HttpServerExchange): Unit = {
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

          val (user, message) = userController.authenticateUser(requestJson)

          if (user != null) {
            //TODO add logic for Successful Registration
            val token = sessionHandler.createSessionTokenForUser(user.userId)


            exchange.setResponseCookie(SecurityUtils.createCookie("ssid", token))

            exchange.getResponseSender.send(JsObject(
              "status" -> JsString("ok"),
              "message" -> JsString("Login successful")
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
            exchange.setStatusCode(400)
            exchange.getResponseSender.send(JsObject(
              "status" -> JsString("failed"),
              "message" -> JsString("Invalid Json Parsing Exception")
            ).prettyPrint)
          }
          case e: Exception => {
            e.printStackTrace()
            exchange.setStatusCode(400)
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
        "message" -> JsString("User is already logged in")
      ).prettyPrint)
    }
  }
}
