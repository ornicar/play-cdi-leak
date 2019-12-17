import play.api._
import play.api.routing.Router

class MyApplicationLoader extends ApplicationLoader {
  def load(context: ApplicationLoader.Context): Application = {
    new MyComponents(context).application
  }
}

class MyComponents(context: ApplicationLoader.Context)
    extends BuiltInComponentsFromContext(context)
    with play.filters.HttpFiltersComponents {

  LoggerConfigurator(context.environment.classLoader).foreach {
    _.configure(context.environment, context.initialConfiguration, Map.empty)
  }

  lazy val homeController: _root_.controllers.HomeController =
    new _root_.controllers.HomeController(
      controllerComponents,
      applicationLifecycle
    )

  lazy val router: Router =
    new _root_.router.Routes(httpErrorHandler, homeController)
}
