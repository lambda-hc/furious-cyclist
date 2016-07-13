package in.lambda_hc.furious_cyclist.rest.controllers


import in.lambda_hc.furious_cyclist.connectors.MysqlClient
import in.lambda_hc.furious_cyclist.models.User
import in.lambda_hc.furious_cyclist.utils.SecurityUtils
import org.slf4j.LoggerFactory
import spray.json.{JsString, JsObject}

import scala.collection.mutable.ArrayBuffer


/**
  * Created by vishnu on 10/6/16.
  *
  * UserController for Doing basic operation on top of User Data
  *
  */
object UserController {
  val LOG = LoggerFactory.getLogger(this.getClass)

  def registerUser(requestJson: JsObject): (User, Array[String]) = {

    var user: User = null

    val messages: scala.collection.mutable.ArrayBuffer[String] = ArrayBuffer.empty[String]
    try {
      //

      val userName = requestJson.getFields("userName").head.asInstanceOf[JsString].value
      val password = requestJson.getFields("password").head.asInstanceOf[JsString].value
      val name = requestJson.getFields("name").head.asInstanceOf[JsString].value
      val email = requestJson.getFields("email").head.asInstanceOf[JsString].value
      val city = requestJson.getFields("city").head.asInstanceOf[JsString].value

      val sanityCheck = MysqlClient.getResultSet("SELECT userName,email from users where username='" + userName + "' || email='" + email + "'")
      if (!sanityCheck.next()) {
        val passwordHash = SecurityUtils.hash(password)
        val userId = MysqlClient.insert(
          tableName = "users",
          elements = Map(
            "username" -> userName,
            "password_hash" -> passwordHash,
            "name" -> name,
            "email" -> email,
            "city" -> city
          )
        )
        user = User.getUser(userId)
        messages += "User Created Successfully with Id " + userId
      } else {
        if (sanityCheck.getString(1).equals(userName)) messages += "userName : " + userName + " Exists !"
        if (sanityCheck.getString(2).equals(email)) messages += "Email id already exists.Please login with new email id"
      }

      //
    } catch {
      case u: UnsupportedOperationException => {
        messages += "Invalid Request Json"
        LOG.debug("Error While Creating user", u)
      }
      case e: Exception => {
        messages += "Sorry unable to process your request please try again after some time"
        LOG.debug("Error While Creating user", e)
      }
    }
    (user, messages.toArray)
  }

  def authenticateUser(email: String, password: String): (Option[User], Array[String]) = {
    val user = User.authenticateAndGetUser(email, password)
    if (user != null)
      (Some(user), Array())
    else
      (None, Array("Invalid Credentials"))
  }

  def authenticateUser(requestJson: JsObject): (User, Array[String]) = {
    var user: User = null

    val messages: scala.collection.mutable.ArrayBuffer[String] = ArrayBuffer.empty[String]
    try {
      val password = requestJson.getFields("password").head.asInstanceOf[JsString].value

      val emailOrUsername: String = if (requestJson.getFields("userName").nonEmpty)
        requestJson.getFields("userName").head.asInstanceOf[JsString].value
      else if (requestJson.getFields("email").nonEmpty)
        requestJson.getFields("email").head.asInstanceOf[JsString].value
      else
        ""
      if (emailOrUsername.nonEmpty)
        user = User.authenticateAndGetUser(emailOrUsername, password)
      else
        messages += "Requests Requires your email or userName"

      if (user == null)
        messages += "Invalid userName or password"

    } catch {
      case u: UnsupportedOperationException => {
        messages += "Invalid Request Json"
        LOG.debug("Error While user login", u)
      }
      case e: Exception => {
        messages += "Sorry unable to process your request please try again after some time"
        LOG.debug("Error While user login", e)
      }
    }

    (user, messages.toArray)
  }

}
