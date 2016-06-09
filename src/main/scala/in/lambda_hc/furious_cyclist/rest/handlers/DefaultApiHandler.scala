package in.lambda_hc.furious_cyclist.rest.handlers

import io.undertow.server.{HttpServerExchange, HttpHandler}

/**
  * Created by vishnu on 20/1/16.
  */
class DefaultApiHandler extends HttpHandler {
  override def handleRequest(exchange: HttpServerExchange): Unit = {
    exchange.getResponseSender.send("{\"status\":\"failed\",\"message\":\"invalid url pattern\"}")
  }
}
