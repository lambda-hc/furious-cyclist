package in.lambda_hc.furious_cyclist.rest.spray.utils

import in.lambda_hc.furious_cyclist.ServerBootstrap
import in.lambda_hc.furious_cyclist.models.User
import spray.http.HttpCookie
import spray.routing.Directives.{extract, pass}
import spray.routing.Directives._
import spray.routing.{Directive0, Directive1}

/**
  * Created by vishnu on 12/7/16.
  */
trait UserDirective {
  /**
    * getUser
    * usage in routes getUser { user => }
    *
    * @return
    */
  def getUser: Directive1[Option[User]] = {
    extract[Option[User]](ctx => {
      val cookies = ctx.request.cookies.filter(_.name == "ssid")
      if (cookies.nonEmpty) {
        ServerBootstrap.sessionHandler.getUserForSession(cookies.head.content)
      } else {
        None
      }
    }
    )
  }

  def deAuthorizeUser: Directive0 = {
    deleteCookie("ssid")
  }

  def authorizeUser(user: User): Directive0 = {
    setCookie(
      HttpCookie("ssid", content = ServerBootstrap.sessionHandler.createSessionTokenForUser(user.userId)
      )
    )
  }

}
