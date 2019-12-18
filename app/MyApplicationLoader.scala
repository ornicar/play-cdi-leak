import play.api._
import play.api.routing.Router
import akka.actor._
import reactivemongo.api._
import scala.concurrent._
import scala.concurrent.duration._

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

  val driver = new AsyncDriver(); // driver.close()

  val res = Await.result(
    driver.connect("mongodb://127.0.0.1:27017/lichess"),
    3.seconds
  )
  println(res)
  applicationLifecycle.addStopHook { () =>
    driver.close().andThen { _ =>
      println("closed driver")
    }
  }

  lazy val httpFilters = Seq()
  // val system = ActorSystem("leak"); system.terminate()

  // lazy val homeController: _root_.controllers.HomeController =
  //   new _root_.controllers.HomeController(
  //     controllerComponents
  //   )

  lazy val router: Router =
    new _root_.router.Routes(httpErrorHandler)
}
