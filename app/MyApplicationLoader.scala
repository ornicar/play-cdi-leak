import java.util.concurrent.atomic.AtomicReference

import play.api._
import play.api.routing.Router
import io.lettuce.core._
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection

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

  // This line causes a classloader leak because it's not managed inside the
  // application lifecycle.
  // See https://www.playframework.com/documentation/2.7.x/ScalaDependencyInjection#Stopping/cleaning-up
  Foo.conn.set(redisClient.connectPubSub())

  applicationLifecycle.addStopHook { () =>
    scala.concurrent.Future {
      Foo.conn.set(null)
      redisClient.shutdown()
    }
  }
}

object Foo {
  var conn = new AtomicReference[StatefulRedisPubSubConnection[String, String]]()
}
