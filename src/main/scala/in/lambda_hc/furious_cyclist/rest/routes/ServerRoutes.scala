package in.lambda_hc.furious_cyclist.rest.routes

import com.google.inject.Inject
import in.lambda_hc.furious_cyclist.rest.handlers.DefaultApiHandler
import io.undertow.server.HttpHandler
import io.undertow.server.handlers.{RedirectHandler, PathHandler}

/**
  * Created by vishnu on 9/6/16.
  */
//TODO make userController Singleton Remove DI
class ServerRoutes @Inject()(
                              defaultApiHandler: DefaultApiHandler,
                              apiRoutes: ApiRoutes
                            ) {

  def getAllHandlers: HttpHandler = {
    new PathHandler()
      .addExactPath("/", new RedirectHandler("https://github.com/lambda-hc/furious-cyclist/blob/master/API-Specs.md"))
      .addPrefixPath("api", apiRoutes.pathHandler)
  }

}
