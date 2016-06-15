package in.lambda_hc.furious_cyclist.rest.undertow.routes

import com.google.inject.Inject
import in.lambda_hc.furious_cyclist.rest.undertow.handlers.DefaultApiHandler

import io.undertow.server.handlers.PathHandler

/**
  * Created by vishnu on 9/6/16.
  */
class ApiRoutes @Inject()(
                           defaultApiHandler: DefaultApiHandler,
                           authenticationRoutes: AuthenticationRoutes
                         ) {
  val pathHandler: PathHandler = new PathHandler()
    .addExactPath("/", defaultApiHandler)
    .addPrefixPath("/auth", authenticationRoutes.pathHandler)
}
