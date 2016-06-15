package in.lambda_hc.furious_cyclist.rest.undertow.routes

import com.google.inject.Inject
import in.lambda_hc.furious_cyclist.rest.undertow.handlers.DefaultApiHandler
import io.undertow.server.HttpHandler
import io.undertow.server.handlers.PathHandler

/**
  * Created by vishnu on 9/6/16.
  */
class ServerRoutes @Inject()(
                              defaultApiHandler: DefaultApiHandler,
                              apiRoutes: ApiRoutes
                            ) {

  def getAllHandlers: HttpHandler = {
    new PathHandler()
      .addExactPath("/", defaultApiHandler)
      .addPrefixPath("api", apiRoutes.pathHandler)
  }

}
