import play.api._
import play.api.routing.Router
import scala.concurrent._
import play.api.libs.ws.WSClient

class MyApplicationLoader extends ApplicationLoader {
  def load(context: ApplicationLoader.Context): Application = {
    new MyComponents(context).application
  }
}

class MyComponents(context: ApplicationLoader.Context)
    extends BuiltInComponentsFromContext(context)
    with play.api.libs.ws.ahc.AhcWSComponents {

  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }

  // This line causes a classloader leak,
  // and prevents any memory from being reclaimed after a run.
  // This causes OOM after a few runs, depending on the application size,
  // and makes play dev workflow unusable.
  val r = A.foo(wsClient)

  lazy val httpFilters = Seq()

  lazy val router: Router = new _root_.router.Routes(httpErrorHandler)
}

object A {
  import ExecutionContext.Implicits.global
  def foo(ws: WSClient) =
    ws.url("https://scala-lang.org").get().map(_ => {})
}
