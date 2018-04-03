package controllers

import javax.inject.Inject

import models.UserForm
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.{UserData, UserService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class LoginController @Inject()(cc: ControllerComponents,
                                userService: UserService,
                                userForms: UserForm)
    extends AbstractController(cc) with I18nSupport {

  def loginForm(): Action[AnyContent] = Action {
    implicit request =>
      Ok(views.html.login(userForms.loginForm))
  }

  def forgetPassword(): Action[AnyContent] = Action {

    implicit request =>
      Ok(views.html.forgetPassword(userForms.forgetPasswordForm))
  }

  def changePassword(): Action[AnyContent] = Action.async {
    implicit request =>
      userForms.forgetPasswordForm.bindFromRequest().fold(
        formWithError => {
          Future.successful(BadRequest(views.html.forgetPassword(formWithError)))
        },
        data => {
          userService.fetchByUsername(data.username) flatMap {
            case Some(_: UserData) => userService.updatePassword(data.username, data.newPassword).map {
              case true =>
                Redirect(routes.HomeController.profile()).flashing("success" -> "password updated successfully")
              case false => InternalServerError("Could not update password")
            }
            case None => Future.successful(Redirect(routes.LoginController.forgetPassword()).flashing("failure" -> "user does not exist"))
          }
        })
  }

  def loginCheck(): Action[AnyContent] = Action.async { implicit request =>
    userForms.loginForm.bindFromRequest().fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.login(formWithErrors)))
      },
      data => {
        val result = userService.validateUser(data.username, data.password)
        result.map {
          case Some(user: UserData) => {
            val adminCheck = userService.isAdmin(data.username)
            val isAdmin = Await.result(adminCheck, Duration.Inf)
            val activeCheck = userService.isEnabled(data.username)
            val isActive = Await.result(activeCheck, Duration.Inf)
            if (isAdmin) {
              Redirect(routes.AdminController.profile()).withSession("username" -> user.username)
            }
            else {
              if (isActive) {
                Redirect(routes.HomeController.profile()).withSession("username" -> user.username)
              }
              else {
                Redirect(routes.HomeController.login()).flashing("isEnable" -> "User Disabled")
              }
            }
          }
          case _ => Redirect(routes.HomeController.login())
              .flashing("invalid" -> "invalid username or password")
        }
      }
    )
  }
}


