package in.lambda_hc.furious_cyclist.rest.controllers.session

import in.lambda_hc.furious_cyclist.models.User
import spray.json.JsObject

/**
  * Created by vishnu on 12/6/16.\
  *
  * Interface for Handling Session. All session Handlers should follow this interface.
  *
  */
trait SessionHandler {

  def getUserIdForSession(sessionToken: String): Long

  def getUserForSession(sessionToken: String): Option[User] = {
    val userID = getUserIdForSession(sessionToken)
    if (userID != 0)
      User.getUser(userID) match {
        case null => {
          println("unable to get user for session " + userID)
          None
        }
        case user: User => {
          println("Got user for session " + user.email)
          Some(user)
        }
      }
    else{
      println("0 returned while getting userID")
      None
    }
  }

  def createSessionTokenForUser(userId: Long): String

  def clearSessionToken(token: String): Unit

  def clearAllSessions: Boolean

  def getAllSessions: JsObject
}
