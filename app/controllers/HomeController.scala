package controllers

import javax.inject.Inject
import play.api.inject.ApplicationLifecycle
import play.api.mvc._
import akka.actor._
import reactivemongo.api._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
class HomeController(cc: ControllerComponents)(
    implicit ec: scala.concurrent.ExecutionContext
) extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}

// os    | java | driver | leak
// ----------------------------
// linux   8
// linux   8      x
// linux   9               x
// linux   9      x        x
// linux   11
// linux   11     x        x
// linux   13
// linux   13     x        x
