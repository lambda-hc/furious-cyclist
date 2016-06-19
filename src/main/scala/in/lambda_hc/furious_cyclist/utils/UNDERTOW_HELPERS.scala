package in.lambda_hc.furious_cyclist.utils

import io.undertow.util.HttpString

/**
  * Created by vishnu on 19/6/16.
  */
object UNDERTOW_HELPERS {
  val ACCESS_CONTROL_ALLOW_ORIGIN = (new HttpString("Access-Control-Allow-Origin"), "*")
  val ACCESS_CONTROL_ALLOW_HEADERS = (new HttpString("Access-Control-Allow-Headers"), "origin, content-type, accept, authorization")
  val ACCESS_CONTROL_ALLOW_CREDENTIALS = (new HttpString("Access-Control-Allow-Credentials"), "true")
  val ACCESS_CONTROL_ALLOW_METHODS = (new HttpString("Access-Control-Allow-Methods"), "GET, POST, PUT, DELETE, OPTIONS, HEAD")
  val ACCESS_CONTROL_MAX_AGE = (new HttpString("Access-Control-Max-Age"), "1209600")
}
