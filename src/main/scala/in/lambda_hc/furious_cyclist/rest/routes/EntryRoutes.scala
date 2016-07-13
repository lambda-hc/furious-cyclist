package in.lambda_hc.furious_cyclist.rest.routes

import com.google.inject.Inject
import in.lambda_hc.furious_cyclist.rest.handlers.DefaultApiHandler
import in.lambda_hc.furious_cyclist.rest.handlers.entries.{GetEntriesHandler, RegisterComplaintHandler}
import io.undertow.server.handlers.PathHandler

/**
  * Created by vishnu on 18/6/16.
  */
//TODO make userController Singleton Remove DI
class EntryRoutes @Inject()(
                             defaultApiHandler: DefaultApiHandler,
                             registerComplaintHandler: RegisterComplaintHandler,
                             getEntriesHandler: GetEntriesHandler
                           ) {

  val pathHandler: PathHandler = new PathHandler()
    .addExactPath("/", defaultApiHandler)
    .addExactPath("/register", registerComplaintHandler)
    .addExactPath("/get", getEntriesHandler)

}
