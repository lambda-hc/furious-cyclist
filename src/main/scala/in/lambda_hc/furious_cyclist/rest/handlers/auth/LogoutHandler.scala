package in.lambda_hc.furious_cyclist.rest.handlers.auth

import java.util.Date

import com.google.inject.Inject
import in.lambda_hc.furious_cyclist.rest.controllers.UserController
import in.lambda_hc.furious_cyclist.rest.controllers.session.SessionHandler
import in.lambda_hc.furious_cyclist.utils.SecurityUtils
import io.undertow.server.{HttpHandler, HttpServerExchange}
import spray.json.{JsObject, JsString}

/**
  * Created by vishnu on 12/6/16.
  */
//TODO make userController Singleton Remove DI
class LogoutHandler @Inject()(userController: UserController) extends HttpHandler {
  override def handleRequest(exchange: HttpServerExchange): Unit = {

    val cookie = SecurityUtils.createCookie("ssid", "").setExpires(new Date(System.currentTimeMillis() - 10000))

    exchange.setResponseCookie(cookie)

    exchange.getResponseSender.send(JsObject(
      "status" -> JsString("ok"),
      "message" -> JsString("User has been logged out")
    ).prettyPrint)

  }
}
