package controllers

import models.{UserForm, UserStoreData}
import org.mockito.Mockito.when
import org.scalatestplus.play.PlaySpec
import org.specs2.mock.Mockito
import play.api.mvc.ControllerComponents
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.{AssignmentService, UserData, UserService}

import scala.concurrent.Future

class HomeControllerSpec extends PlaySpec with Mockito {

  val controller = getMockedObject

  "Home controller" should {

    "bad request" in {

      val user = UserStoreData("randhir", Some("kk"), "kumar", "randhir", "123456", "123456", "9953188803", "male", 23, "cricket")
      val userForm = new UserForm {}.registrationForm.fill(user)
      when(controller.userForm.registrationForm) thenReturn userForm
      val userData = UserData(1, "randhir", Some("kk"), "kumar", "randhir", "123456", "9953188803", "male", 23, "cricket", true, false)
      when(controller.userService.addUser(userData)) thenReturn Future.successful(true)

      val request = FakeRequest(POST, "/storeUserData").withFormUrlEncodedBody("csrfToken"
          -> "9c48f081724087b31fcf6099b7eaf6a276834cd9-1487743474314-cda043ddc3d791dc500e66ea",
        "fisrtName" -> "randhir",
        "middleName" -> "kk",
        "lastName" -> "kumar",
        "username" -> "randhir",
        "password" -> "12345",
        "mobile" -> "9953188803",
        "gender" -> "male",
        "age" -> "30",
        "hobbies" -> "cricket"
      )
          .withCSRFToken

      val result = controller.homeController.storeUserData()(request)
      status(result) mustBe 400
    }

    "add user into database" in {

      val userInformation = UserData(1, "randhir", Some("kk"), "kumar", "randhir", "123456", "9953188803", "male", 23, "cricket", true, false)

      when(controller.userService.fetchByUsername("randhir")) thenReturn Future.successful(Option(userInformation))

      when(controller.userService.addUser(userInformation)) thenReturn Future.successful(true)

      val request = FakeRequest(POST, "/storeUserData").withFormUrlEncodedBody("csrfToken"
          -> "9c48f081724087b31fcf6099b7eaf6a276834cd9-1487743474314-cda043ddc3d791dc500e66ea",
        "firstName" -> "randhir",
        "middleName" -> "kk",
        "lastName" -> "kumar",
        "username" -> "randhir",
        "password" -> "12345",
        "rePassword" -> "12345",
        "mobile" -> "9953188803",
        "gender" -> "male",
        "age" -> "23",
        "hobbies" -> "coding")
          .withCSRFToken

      val result = controller.homeController.storeUserData().apply(request)
      status(result) mustBe 303
    }

  }

  def getMockedObject: TestObjects = {
    val assignmentService = mock[AssignmentService]
    val userForm = mock[UserForm]
    val userService = mock[UserService]

    val controller = new HomeController(assignmentService, userForm, userService, stubControllerComponents())

    TestObjects(assignmentService, userForm, userService, stubControllerComponents(), controller)
  }

  case class TestObjects(assignmentService: AssignmentService,
                         userForm: UserForm,
                         userService: UserService,
                         controllerComponent: ControllerComponents,
                         homeController: HomeController
                        )


}

