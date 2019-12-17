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

  // this obviously fails at runtime, and then the MyComponents class
  // remains forever loaded. There is no classloader GC between two `run`s.
  // this eventually leads to OOM.
  // It can be observed with:
  // jmap -clstats $(jps | grep sbt-launch.jar | cut -f1 -d' ') | egrep MyComponents
  // run the app, stop with enter, run again.
  // Then jmap -clstats will show all classes loaded  twice.
  lazy val homeController: _root_.controllers.HomeController = ???

  lazy val router: Router =
    new _root_.router.Routes(httpErrorHandler, homeController)
}
