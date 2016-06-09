package in.lambda_hc.furious_cyclist.rest.routes

import com.google.inject.Inject
import in.lambda_hc.furious_cyclist.rest.handlers.DefaultApiHandler
import io.undertow.server.handlers.PathHandler

/**
  * Created by vishnu on 9/6/16.
  */
class ApiRoutes @Inject()(
                           defaultApiHandler: DefaultApiHandler
                         ) {
  val pathHandler: PathHandler = new PathHandler()
    .addExactPath("/", defaultApiHandler)
}
