package in.lambda_hc.furious_cyclist.rest.spray.utils


import spray.http.StatusCodes
import spray.routing.{StandardRoute, HttpService}

/**
  * Created by vishnu on 13/7/16.
  */
trait ServerUtils extends HttpService with UserDirective{

  private def redirect(route:Route):StandardRoute = redirect(route.url,StatusCodes.TemporaryRedirect)

  val TO_DASHBOARD_PAGE = redirect(ServerUtils.DASHBOARD_PAGE)
  val TO_LOGIN_PAGE = redirect(ServerUtils.LOGIN_PAGE)

}

object ServerUtils {

  private val DASHBOARD_PAGE = new Route("/home")
  private val LOGIN_PAGE = new Route("/login")

}

case class Route(val url: String)