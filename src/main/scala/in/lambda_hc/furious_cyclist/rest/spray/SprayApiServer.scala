package in.lambda_hc.furious_cyclist.rest.spray

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.google.inject.Inject
import com.typesafe.config.Config
import in.lambda_hc.furious_cyclist.ServerBootstrap
import in.lambda_hc.furious_cyclist.rest.ServerInterface
import org.slf4j.{Logger, LoggerFactory}
import spray.can.Http

import scala.concurrent.duration._

/**
  * Created by vishnu on 15/6/16.
  */
class SprayApiServer @Inject()(config: Config) extends ServerInterface {
  private val LOG: Logger = LoggerFactory.getLogger(classOf[SprayApiServer])
  LOG.info("Loading Spray Server Module")

  override var isServerActive: Boolean = false

  implicit val system: ActorSystem = ServerBootstrap.actorSystem
  val port = config.getInt("server.api.port")
  val interface = config.getString("server.api.interface")
  val service = system.actorOf(Props[ServerServiceActor], "API-SERVICE")

  override def startServer: Boolean = {
    if (isServerActive) {
      LOG.error("Server is already active and is running on http://" + interface + ":/" + port)
      isServerActive
    } else {
      implicit val timeout = Timeout(config.getInt("server.api.timeout").seconds)
      LOG.info("Starting Server on http://" + interface + ":" + port)
      IO(Http) ? Http.Bind(service, interface = interface, port = port)
      isServerActive = true
      isServerActive
    }
  }

  override def stopServer: Boolean = {
    val port = config.getInt("ml.api-server.port")
    val interface = config.getString("ml.api-server.interface")
    LOG.info("Terminating Server Running on " + interface + ":" + port)
    system.stop(service)
    LOG.info("Terminated Server Running on " + interface + ":" + port)
    true
  }

}
