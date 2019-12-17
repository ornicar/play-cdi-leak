package controllers

import javax.inject.Inject
import play.api.mvc._
import play.api.inject.ApplicationLifecycle
import reactivemongo.api._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
class HomeController(cc: ControllerComponents, lifecycle: ApplicationLifecycle)(
    implicit ec: scala.concurrent.ExecutionContext
) extends AbstractController(cc) {

  /* Play logging:
   * ----------------
   * instanciated driver
   * [info] play.api.Play - Application started (Dev) (no global state)
   * [info] p.c.s.AkkaHttpServer - Stopping server...
   * closed driver
   * ----------------
   * No memory is reclaimed between two `run`s.
   * All previous instances and classes remain loaded forever.
   * To observe, run:
   * jmap -clstats \$(jps | grep sbt-launch.jar | cut -f1 -d' ') | egrep HomeController
   */
  val driver = new AsyncDriver()
  println("instanciated driver")

  lifecycle.addStopHook { () =>
    driver.close()
    println("closed driver")
    scala.concurrent.Future.successful({})
  }
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
