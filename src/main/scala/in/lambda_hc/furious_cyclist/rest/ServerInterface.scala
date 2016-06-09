package in.lambda_hc.furious_cyclist.rest

/**
  * Created by vishnu on 9/6/16.
  */
trait ServerInterface {

  var isServerActive: Boolean

  def startServer: Boolean

  def stopServer: Boolean


}
