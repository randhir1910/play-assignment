package controllers

import javax.inject.Inject

import models.UserForm
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.{Assignment, AssignmentService, UserService}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class AdminController @Inject()(cc: ControllerComponents,
                                assignmentService: AssignmentService,
                                userService: UserService,
                                userForm: UserForm)
    extends AbstractController(cc) with I18nSupport {


  def profile() = Action.async { implicit request: Request[AnyContent] =>
    Future.successful(Ok(views.html.adminProfile()))
  }

  def logout() = Action { implicit request: Request[AnyContent] =>
    Ok("Bye").withNewSession
    Ok(views.html.login(userForm.loginForm))
  }

  def showAssignment() = Action .async{
    implicit request =>
       Future.successful(Ok(views.html.addAssignment(userForm.assignmentForm)))
  }

  def showUser() = Action.async{
    implicit request =>
      userService.getAllUser.map {
        userList => Ok(views.html.showUser(userList))
      }
  }

  def createAssignment() = Action.async {
    implicit request =>
      userForm.assignmentForm.bindFromRequest().fold(
        formWithError => {
          Future.successful(BadRequest(views.html.addAssignment(formWithError)))
        },
        data => {
          val assignment = Assignment(0, data.title, data.description)
          assignmentService.createAssignment(assignment).map {
            case true => Redirect(routes.AdminController.profile()).flashing("success" -> "Assignment Added")
            case false => InternalServerError("couldn't add assignment")
          }
        }
      )
  }

  def readAssignment() = Action.async {
    implicit request =>
      assignmentService.readAssignment.map {
        assignmentsList => Ok(views.html.adminAssignment(assignmentsList))
      }
  }

  def deleteAssignment(id: Int) = Action.async {
    implicit request =>
      assignmentService.deleteAssignment(id).map {
        case true => Redirect(routes.AdminController.profile()).flashing("success" -> "Assignment Deleted")
        case false => InternalServerError("not able to delete")
      }
  }

  def isActiveCheck(updatedValue:Boolean):String = {
    if(updatedValue) "user enable successfully" else "user disable successfully"
  }

  def enableOrDisableUser(username: String, updatedValue: Boolean) = Action.async {
    implicit request =>
      userService.enableDisableUser(username, updatedValue).map {
        case true => Redirect(routes.AdminController.profile()).flashing("success" -> isActiveCheck(updatedValue))
        case false => InternalServerError("couldn't enable or disable user")
      }
  }

}
