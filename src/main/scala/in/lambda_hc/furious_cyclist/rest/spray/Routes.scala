package in.lambda_hc.furious_cyclist.rest.spray

import spray.routing.HttpService

/**
  * Created by vishnu on 15/6/16.
  */
trait Routes extends HttpService{

  def rootRoute = pathPrefix("api") {
    complete("Api Server 1")
  } ~ complete("Api Server")

}
