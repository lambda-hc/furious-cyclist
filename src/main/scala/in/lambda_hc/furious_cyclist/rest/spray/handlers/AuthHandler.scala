package in.lambda_hc.furious_cyclist.rest.spray.handlers

import in.lambda_hc.furious_cyclist.rest.controllers.UserController
import in.lambda_hc.furious_cyclist.rest.spray.utils.ServerUtils
import spray.http.{HttpCookie, StatusCodes, FormData}
import spray.httpx.PlayTwirlSupport._

/**
  * Created by vishnu on 16/6/16.
  */
trait AuthHandler extends ServerUtils {

  def logoutHandler = deAuthorizeUser {
    TO_DASHBOARD_PAGE
  }


  def loginPageHandler = getUser {
    case Some(user) => TO_DASHBOARD_PAGE
    case None => get {
      complete {
        html.Login.render(Array())
      }
    } ~ post {
      entity(as[FormData]) {
        form => {
          val data = form.fields.toMap

          data.get("username") match {
            case Some(userName) => data.get("password") match {
              case Some(password) => {
                val (userObj, message) = UserController.authenticateUser(userName, password)
                userObj match {
                  case Some(user) => authorizeUser(user) {
                    TO_DASHBOARD_PAGE
                  }
                  case None => complete {
                    html.Login.render(message)
                  }
                }
              }
              case None => complete {
                html.Login.render(Array("Invalid Parameters"))
              }
            }
            case None => complete {
              html.Login.render(Array("Invalid Parameters"))
            }
          }

        }
      }
    }
  }

}
