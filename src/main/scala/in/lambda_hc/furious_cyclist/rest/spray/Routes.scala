package in.lambda_hc.furious_cyclist.rest.spray

import in.lambda_hc.furious_cyclist.models.User
import in.lambda_hc.furious_cyclist.rest.spray.handlers.AuthHandler
import in.lambda_hc.furious_cyclist.rest.spray.utils.{ServerUtils}
import spray.json.{JsArray, JsString, JsObject}
import spray.routing.HttpService
import spray.httpx.PlayTwirlSupport._

/**
  * Created by vishnu on 15/6/16.
  */
trait Routes extends ServerUtils with AuthHandler {

  def rootRoute = pathPrefix("api") {
    apiRoutes
  } ~ path("login") {
    loginPageHandler
  } ~ path("logout") {
    logoutHandler
  } ~ path("home") {
    complete {
      html.index.render("Home", User.getUser(1))
    }
  } ~ path("viewRecords") {
    complete {

      html.index.render("View Records", null)
    }
  } ~ path("RegisterComplaint") {
    complete {
      html.index.render("Register Complaint", null)
    }
  } ~ pathPrefix("assets") {

    getFromDirectory("assets")

  } ~ complete("Api Server")

  def apiRoutes = complete("default Api Route")
}
