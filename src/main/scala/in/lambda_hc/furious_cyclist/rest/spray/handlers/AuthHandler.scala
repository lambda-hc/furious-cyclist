package in.lambda_hc.furious_cyclist.rest.spray.handlers

import in.lambda_hc.furious_cyclist.ServerBootstrap
import in.lambda_hc.furious_cyclist.rest.controllers.UserController
import spray.routing.HttpService
import spray.json._

/**
  * Created by vishnu on 16/6/16.
  */
trait AuthHandler extends HttpService {

  def login = cookie("ssid") { cookieObj => {
    println("Reading Cookie" + cookieObj)
    val user = ServerBootstrap.sessionHandler.getUserForSession(cookieObj.content)
    if (user == null) {
      createNewUser
    } else {
      complete(user.toJson.prettyPrint)
    }
  }
  } ~ createNewUser

  private def createNewUser = entity(as[String]) { body =>
    val (user, message) = UserController.authenticateUser(body.parseJson.asJsObject)
    if (user != null) {
      complete(user.toJson.prettyPrint)
    } else
      complete(JsObject("status" -> JsString("failed"), "message" -> JsArray(message.map(JsString(_)).toVector)).prettyPrint)
  }

}
