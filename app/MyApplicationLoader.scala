import play.api._
import play.api.routing.Router
import io.lettuce.core._

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

  val redisClient = RedisClient create RedisURI.create("redis://127.0.0.1")

  // This line causes a classloader leak
  Foo.conn = redisClient.connectPubSub()

  applicationLifecycle.addStopHook { () =>
    scala.concurrent.Future {
      redisClient.shutdown()
    }
  }
}

object Foo {
  var conn: pubsub.StatefulRedisPubSubConnection[String, String] = _
}
