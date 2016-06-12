package in.lambda_hc.furious_cyclist


import com.google.inject.Guice
import com.typesafe.config.Config
import in.lambda_hc.furious_cyclist.di.ServerDiModule
import in.lambda_hc.furious_cyclist.models.User
import in.lambda_hc.furious_cyclist.rest.ServerInterface
import in.lambda_hc.furious_cyclist.utils.InitializationUtils

object ServerBootstrap {
  println("Server Initializing")

  val config: Config = InitializationUtils.getConfiguration

  val injector = Guice.createInjector(new ServerDiModule(config))

  val serverInterface = injector.getInstance(classOf[ServerInterface])

  def main(args: Array[String]): Unit = {

    serverInterface.startServer

    println("Main function control Exited")

  }

}
