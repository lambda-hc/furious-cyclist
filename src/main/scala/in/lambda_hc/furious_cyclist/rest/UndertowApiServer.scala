package in.lambda_hc.furious_cyclist.rest

import com.google.inject.Inject
import com.typesafe.config.Config
import in.lambda_hc.furious_cyclist.rest.routes.ServerRoutes
import io.undertow.Undertow
import org.slf4j.{LoggerFactory, Logger}

/**
  * Created by vishnu on 9/6/16.
  */
class UndertowApiServer @Inject()(config: Config, serverRoutes: ServerRoutes) extends ServerInterface {
  override var isServerActive: Boolean = false
  private val LOG: Logger = LoggerFactory.getLogger(classOf[UndertowApiServer])

  private var server: Undertow = null
  private val port = config.getInt("server.api.port")
  private val interfaceName = config.getString("server.api.interface")

  override def startServer: Boolean = {
    if (isServerActive) {
      LOG.error("The Server is Already Active on http://" + interfaceName + ":" + port)
      false
    } else {
      try {
        server = Undertow.builder.addHttpListener(port, interfaceName)
          .setHandler(serverRoutes.getAllHandlers)
          .build

        server.start()
        isServerActive = true
        LOG.info("The Server is Active on http://" + interfaceName + ":" + port)
        true
      } catch {
        case e: Exception => LOG.error("Server Initialization Failed ", e)
          false
      }
    }
  }

  override def stopServer: Boolean = {
    try {
      server.stop()
      server = null
    }
    catch {
      case e: NullPointerException => LOG.debug("Server Has Not been Initialized Shutdown Sequence Complete")
    }
    isServerActive = false
    true
  }
}
