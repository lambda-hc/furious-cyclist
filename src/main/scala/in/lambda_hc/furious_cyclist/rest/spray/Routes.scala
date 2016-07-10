package in.lambda_hc.furious_cyclist.rest.spray

import in.lambda_hc.furious_cyclist.models.User
import in.lambda_hc.furious_cyclist.rest.spray.handlers.AuthHandler
import spray.json.{JsArray, JsString, JsObject}
import spray.routing.HttpService
import spray.httpx.PlayTwirlSupport._

/**
  * Created by vishnu on 15/6/16.
  */
trait Routes extends HttpService with AuthHandler {

  def rootRoute = pathPrefix("api") {
    apiRoutes
  } ~ path("home") {
    complete {
      html.index.render("Home",User.getUser(1))
    }
  }~ path("viewRecords") {
    complete {
      html.index.render("View Records",null)
    }
  }~ path("RegisterComplaint") {
    complete {
      html.index.render("Register Complaint",null)
    }
  } ~ pathPrefix("assets") {

    getFromDirectory("assets")

  } ~ complete("Api Server")

  def apiRoutes = path("login") {
    get {
      complete(JsObject(
        "status" -> JsString("failed"),
        "message" -> JsArray(JsString("Use Get Request"))
      ).prettyPrint)
    }
    post {
      login
    }
  } ~ complete("default Api Route")
}
