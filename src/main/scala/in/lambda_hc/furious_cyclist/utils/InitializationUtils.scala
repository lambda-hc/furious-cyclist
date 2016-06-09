package in.lambda_hc.furious_cyclist.utils

import java.net.{InetAddress, NetworkInterface}
import collection.JavaConversions._

import com.typesafe.config.{ConfigValueFactory, ConfigFactory, Config}

/**
  * Created by vishnu on 9/6/16.
  */
object InitializationUtils {
  // Modify all configuration setting within this function
  def getConfiguration: Config = {

    // Add ip Address to the existing list of Configurations
    val IP_ADDRESS: String = NetworkInterface.getNetworkInterfaces.toIterator.flatMap(_.getInetAddresses.toSeq).find(address =>
      address.getHostAddress.contains(".") && !address.isLoopbackAddress
    ).getOrElse(InetAddress.getLocalHost).getHostAddress

    ConfigFactory.load().withValue("ip", ConfigValueFactory.fromAnyRef(IP_ADDRESS))
  }
}
