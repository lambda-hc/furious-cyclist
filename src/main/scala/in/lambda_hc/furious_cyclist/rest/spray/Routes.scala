package in.lambda_hc.furious_cyclist.rest.spray

import in.lambda_hc.furious_cyclist.rest.spray.handlers.AuthHandler
import in.lambda_hc.furious_cyclist.rest.spray.utils.ServerUtils

import spray.httpx.PlayTwirlSupport._

/**
  * Created by vishnu on 15/6/16.
  */
trait Routes extends ServerUtils with AuthHandler {

  def rootRoute =
    path("login") {
      loginPageHandler

    } ~ path("logout") {
    logoutHandler

  } ~ path("home") {
    getUser { user =>
      complete {
        html.index.render("Home", user)
      }
    }

  } ~ path("viewRecords") {
      getUser { user =>
        complete {
          html.index.render("View Records", user)
        }
      }

  } ~ path("RegisterComplaint") {
      getUser { user =>
        complete {
          html.index.render("Register Complaint", user)
        }
      }
  } ~ pathPrefix("assets") {
    getFromDirectory("assets")

  } ~ complete("Invalid Route 404")

  def apiRoutes = complete("default Api Route")
}
