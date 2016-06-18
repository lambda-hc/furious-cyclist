package in.lambda_hc.furious_cyclist.rest.spray

import in.lambda_hc.furious_cyclist.rest.spray.handlers.AuthHandler
import spray.json.{JsArray, JsString, JsObject}
import spray.routing.HttpService

/**
  * Created by vishnu on 15/6/16.
  */
trait Routes extends HttpService with AuthHandler {

  def rootRoute = pathPrefix("api") {
    apiRoutes
  } ~ complete("Api Server")

  def apiRoutes = path("login") {
    get {
      complete(JsObject(
        "status"->JsString("failed"),
        "message"->JsArray(JsString("Use Get Request"))
      ).prettyPrint)
    }
    post {
      login
    }
  } ~ complete("default Api Route")
}
