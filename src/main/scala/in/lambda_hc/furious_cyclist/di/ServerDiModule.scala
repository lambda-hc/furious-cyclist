package in.lambda_hc.furious_cyclist.di


import com.google.inject.AbstractModule
import com.typesafe.config.Config
import in.lambda_hc.furious_cyclist.connectors.MysqlClient
import in.lambda_hc.furious_cyclist.rest.controllers.session.{InMemorySessionHandler, SessionHandler}
import in.lambda_hc.furious_cyclist.rest.ServerInterface
import in.lambda_hc.furious_cyclist.rest.spray.SprayApiServer
import in.lambda_hc.furious_cyclist.rest.undertow.UndertowApiServer
import net.codingwell.scalaguice.ScalaModule

/**
  * Created by vishnu on 9/6/16.
  */
class ServerDiModule(config: Config) extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[Config].toInstance(config)

    bind[ServerInterface].to[SprayApiServer].asInstanceOf[Singleton]
//    bind[ServerInterface].to[UndertowApiServer].asInstanceOf[Singleton]

    bind[MysqlClient].asInstanceOf[Singleton]
    bind[SessionHandler].to[InMemorySessionHandler].asInstanceOf[Singleton]
  }
}
