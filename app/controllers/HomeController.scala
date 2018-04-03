package controllers

import javax.inject.{Inject, Singleton}

import models.{UserForm, UserProfile}
import play.api.Logger
import play.api.mvc._
import services.{AssignmentService, UserData, UserService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class HomeController @Inject()(assignmentService: AssignmentService
                               , userForm: UserForm, userService: UserService
                               , cc: ControllerComponents) extends AbstractController(cc) {

  def index(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def login(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.login(userForm.loginForm))
  }

  def register(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signUp(userForm.registrationForm))
  }

  def logout(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok("Bye").withNewSession
    Ok(views.html.login(userForm.loginForm))
  }


  def profile(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.userProfile())
  }

  def viewAssignment(): Action[AnyContent] = Action.async {
    implicit request =>
      assignmentService.readAssignment.map {
        assignmentsList => Ok(views.html.userAssignment(assignmentsList))
      }
  }

  def storeUserData: Action[AnyContent] = Action.async { implicit request =>
    userForm.registrationForm.bindFromRequest().fold(
      formWithError => {
        Future.successful(BadRequest(views.html.signUp(formWithError)))
      },
      data => {
        userService.fetchByUsername(data.username).flatMap {
          optionalRecord =>
            optionalRecord.fold {
              val record = UserData(0, data.firstName, data.middleName, data.lastName, data.username,
                data.password, data.mobile, data.gender, data.age, data.hobbies)
              val result = userService.addUser(record)
              result.map {
                case true => Redirect(routes.HomeController.profile())
                    .withSession("name" -> data.firstName)
                    .flashing("success" -> s"welcome ${data.firstName} ${data.lastName}")
                case false => Logger.info("internal error")
                  Redirect(routes.HomeController.index())
              }

            } {
              _ => Future.successful(Redirect(routes.HomeController.login()).flashing("success" -> "user already exist"))

            }
        }
      })
  }


  def updateProfile(): Action[AnyContent] = Action.async { implicit request =>
    val username = request.session.get("username").getOrElse("no exist")
    val userData = userService.fetchByUsername(username)
    userData.map {
      case Some(user) =>
        val userProfile = UserProfile(user.firstName, user.middleName, user.lastName,
          user.mobile, user.gender, user.age, user.hobbies)
        val profileForm = userForm.profileForm.fill(userProfile)
        Ok(views.html.updateData(profileForm))
    }
  }

  def updateUserData(): Action[AnyContent] = Action.async { implicit request =>
    val userName = request.session.get("username").getOrElse("")
    userForm.profileForm.bindFromRequest().fold(
      formWithError => {
        Future.successful(BadRequest(views.html.updateData(formWithError)))
      },
      data => {
        val record = UserProfile(data.firstName, data.middleName, data.lastName, data.mobile
          , data.gender, data.age, data.hobbies)
        val result = userService.updateProfile(userName, record)
        result.map {
          case true => Redirect(routes.HomeController.profile())
              .flashing("success" -> s"details for ${data.firstName} has been updated")
          case false => Redirect(routes.HomeController.profile())
              .flashing("success" -> s"details for ${data.firstName} not updated")
        }
      }
    )
  }
}
