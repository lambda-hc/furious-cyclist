package in.lambda_hc.furious_cyclist.rest.spray

import akka.actor.Actor


/**
  * Created by vishnu on 15/6/16.
  */
class ServerServiceActor extends Actor with Routes {
  def actorRefFactory = context
  def receive = runRoute(rootRoute)
}
