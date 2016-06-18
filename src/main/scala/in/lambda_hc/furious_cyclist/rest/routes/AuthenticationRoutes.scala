package in.lambda_hc.furious_cyclist.rest.routes

import com.google.inject.Inject
import in.lambda_hc.furious_cyclist.rest.handlers.DefaultApiHandler
import in.lambda_hc.furious_cyclist.rest.handlers.auth.{LogoutHandler, LoginHandler, RegisterHandler}
import io.undertow.server.handlers.PathHandler

/**
  * Created by vishnu on 12/6/16.
  */
//TODO make userController Singleton Remove DI

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
