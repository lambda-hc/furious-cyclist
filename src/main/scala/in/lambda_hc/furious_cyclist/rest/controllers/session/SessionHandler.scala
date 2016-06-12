package in.lambda_hc.furious_cyclist.rest.controllers.session

import in.lambda_hc.furious_cyclist.models.User
import spray.json.JsObject

/**
  * Created by vishnu on 12/6/16.
  */
trait SessionHandler {

  def getUserIdForSession(sessionToken: String): Long

  def getUserForSession(sessionToken: String): User = {
    val userID = getUserIdForSession(sessionToken)
    if (userID != 0)
      User.getUser(userID)
    else
      null
  }

  def createSessionTokenForUser(userId: Long): String

  def clearSessionToken(token: String): Unit

  def clearAllSessions: Boolean

  def getAllSessions: JsObject
}
