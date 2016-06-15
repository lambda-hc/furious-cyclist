package in.lambda_hc.furious_cyclist.rest.undertow.routes

import com.google.inject.Inject
import in.lambda_hc.furious_cyclist.rest.undertow.handlers.DefaultApiHandler
import in.lambda_hc.furious_cyclist.rest.undertow.handlers.auth.{LogoutHandler, RegisterHandler, LoginHandler}

import io.undertow.server.handlers.PathHandler

/**
  * Created by vishnu on 12/6/16.
  */
class AuthenticationRoutes @Inject()(
                                      defaultApiHandler: DefaultApiHandler,
                                      registerHandler: RegisterHandler,
                                      loginHandler: LoginHandler,
                                      logoutHandler: LogoutHandler
                                    ) {

  val pathHandler = new PathHandler()
    .addExactPath("/", defaultApiHandler)
    .addExactPath("/register", registerHandler)
    .addExactPath("/login", loginHandler)
    .addExactPath("/logout", logoutHandler)

}
