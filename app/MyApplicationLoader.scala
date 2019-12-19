import play.api._
import play.api.routing.Router

class MyApplicationLoader extends ApplicationLoader {
  def load(context: ApplicationLoader.Context): Application = {
    new MyComponents(context).application
  }
}

class MyComponents(context: ApplicationLoader.Context)
    extends BuiltInComponentsFromContext(context) {

  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }

  lazy val httpFilters = Seq.empty
  lazy val router: Router = new _root_.router.Routes(httpErrorHandler)
}
