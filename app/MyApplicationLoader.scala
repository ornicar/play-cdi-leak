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

  import io.lettuce.core._
  // This line causes a classloader leak,
  // and prevents any memory from being reclaimed after a run.
  // This causes OOM after a few runs, depending on the application size,
  // and makes play dev workflow unusable.
  val redisClient = RedisClient create RedisURI.create("redis://127.0.0.1")
  val conn = redisClient.connectPubSub()
  Bus.send = s => conn.async.publish("chan", s)
  applicationLifecycle.addStopHook { () =>
    scala.concurrent.Future {
      redisClient.shutdown()
      Bus.send = s => {}
      println("Stopped the socket redis pool.")
    }
  }

  lazy val httpFilters = Seq()

  lazy val router: Router = new _root_.router.Routes(httpErrorHandler)
}

object Bus {
  var send: String => Unit = _
}
