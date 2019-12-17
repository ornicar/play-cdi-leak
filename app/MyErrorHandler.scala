import javax.inject._

import play.api.http.DefaultHttpErrorHandler
import play.api._
import play.api.mvc._
import play.core.SourceMapper
import play.api.mvc.Results._
import play.api.routing.Router
import scala.concurrent._

@Singleton
class MyErrorHandler @Inject() (
    environment: Environment,
    configuration: Configuration,
    sourceMapper: Option[SourceMapper],
    router: => Option[Router]
)(implicit ec: ExecutionContext)
    extends DefaultHttpErrorHandler(
      environment,
      configuration,
      sourceMapper,
      router
    ) {

  println("-- loaded")

}
