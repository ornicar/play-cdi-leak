package controllers

import play.api.mvc._

class HomeController(cc: ControllerComponents)(
    implicit ec: scala.concurrent.ExecutionContext
) extends AbstractController(cc) {

  def index = Action {
    Ok("index")
  }

}
