package in.lambda_hc.furious_cyclist.rest.controllers.session

import in.lambda_hc.furious_cyclist.utils.SecurityUtils
import org.slf4j.{LoggerFactory, Logger}
import spray.json.{JsNumber, JsObject}

/**
  * Created by vishnu on 12/6/16.
  */
object InMemorySessionHandler extends SessionHandler {
  // String Token Key (userId, Expiration Time)
  val cache: scala.collection.mutable.HashMap[String, (Long, Long)] = scala.collection.mutable.HashMap[String, (Long, Long)]()

  private val LOG: Logger = LoggerFactory.getLogger(this.getClass)

  override def getUserIdForSession(sessionToken: String): Long = {
    LOG.debug("gettingUserFor Session " + sessionToken)
    val sessions = InMemorySessionHandler.cache.get(sessionToken)
    if (sessions.isEmpty) 0 else sessions.head._1
  }

  override def createSessionTokenForUser(userId: Long): String = {

    val token = SecurityUtils.generateSHAToken(userId.toString)
    val filteredSessions = InMemorySessionHandler.cache.filter(i => i._2._1 == userId)
    if (filteredSessions.isEmpty) {
      InMemorySessionHandler.cache.put(token, (userId, System.currentTimeMillis()))
    } else {
      filteredSessions.foreach(session => InMemorySessionHandler.cache.remove(session._1))
      InMemorySessionHandler.cache.put(token, (userId, System.currentTimeMillis()))
    }
    LOG.debug("createSessionTokenForUser " + userId + " " + token)
    token
  }

  override def clearAllSessions: Boolean = {
    InMemorySessionHandler.cache.clear()
    true
  }

  override def getAllSessions: JsObject = {
    val response = new JsObject(InMemorySessionHandler.cache.map(i => i._1 -> JsNumber(i._2._1)).toMap)
    println(response)
    response
  }

  override def clearSessionToken(token: String): Unit = {
    InMemorySessionHandler.cache.remove(token)
  }
}
