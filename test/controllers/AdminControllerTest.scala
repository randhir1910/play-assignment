package controllers

import models.{AssignmentData, UserForm}
import org.mockito.Mockito.when
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.mvc.ControllerComponents
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.{Assignment, AssignmentService, UserService}

import scala.concurrent.Future

class AdminControllerTest extends PlaySpec with Mockito {


  val controller = getMockedObject

  "Admin controller" should {

    "add assignment into database" in {

      val assignment = Assignment(0, "c++ program", "max subarray value")

      val formData = AssignmentData("c++ program", "max subarray value")
      val form = new UserForm {}.assignmentForm.fill(formData)

      when(controller.userForm.assignmentForm) thenReturn form
      when(controller.assignmentService.createAssignment(assignment)) thenReturn Future.successful(true)

      val request = FakeRequest(POST, "/addAssignment").withFormUrlEncodedBody("csrfToken"
          -> "9c48f081724087b31fcf6099b7eaf6a276834cd9-1487743474314-cda043ddc3d791dc500e66ea", "title" -> "c++ program", "description" -> "max subarray value")
          .withCSRFToken

      val result = controller.adminController.createAssignment()(request)
      status(result) mustBe SEE_OTHER
    }

    "bad request if assignment form fill blank" in {

      val request = FakeRequest(POST, "/addAssignment").withFormUrlEncodedBody("csrfToken"
          -> "cda043ddc3d791dc500e66ea", "title" -> "", "description" -> "")
          .withCSRFToken

      val result = controller.adminController.createAssignment().apply(request)
      status(result) mustBe 400

    }
    "delete assignment" in {

      when(controller.assignmentService.deleteAssignment(1)) thenReturn Future.successful(true)
      val request = FakeRequest(GET, "/deleteAssignment")
      val result = controller.adminController.deleteAssignment(1).apply(request)
      status(result) mustBe SEE_OTHER
    }


    "not able to delete assignment" in {

      when(controller.assignmentService.deleteAssignment(3)) thenReturn Future.successful(false)
      val request = FakeRequest(GET, "/deleteAssignment")
      val result = controller.adminController.deleteAssignment(3).apply(request)
      status(result) mustBe 500
    }

  }

  def getMockedObject: TestObjects = {
    val assignmentService = mock[AssignmentService]
    val userForm = mock[UserForm]
    val userService = mock[UserService]

    val controller = new AdminController(stubControllerComponents(), assignmentService, userService, userForm)

    TestObjects(stubControllerComponents(), assignmentService, userService, userForm, controller)
  }

  case class TestObjects(controllerComponent: ControllerComponents,
                         assignmentService: AssignmentService,
                         userService: UserService,
                         userForm: UserForm,
                         adminController: AdminController
                        )


}
