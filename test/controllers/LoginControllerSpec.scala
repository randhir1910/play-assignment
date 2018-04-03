package controllers

import models.UserForm
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.mvc.ControllerComponents
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import play.api.test.Helpers.{stubControllerComponents, _}
import services.{UserData, UserService}

import scala.concurrent.Future

class LoginControllerSpec extends PlaySpec with MockitoSugar {

  val controller = getMockedObject

  "login the valid user" in {

    val userData = UserData(1, "randhir", Some("kk"), "kumar", "randhir", "12345", "9953188803", "male", 23, "cricket", true, false)
    when(controller.userService.fetchByUsername("randhir")) thenReturn Future.successful(Option(userData))

    when(controller.userService.validateUser("randhir", "12345")) thenReturn Future.successful(Some(UserData(1, "randhir", Some("Kumar"), "Singh", "randhir", "12345", "9953188803", "male", 24, "reading")))

    val request = FakeRequest(POST, "/loginCheck").withFormUrlEncodedBody("csrfToken"
        -> "9c48f081724087b31f", "username" -> "randhir", "password" -> "12345")
        .withCSRFToken

    val result = controller.loginController.loginCheck().apply(request)
    status(result) mustBe SEE_OTHER
    redirectLocation(result) mustBe Some("/profile")
  }

  def getMockedObject: TestObjects = {
    val userForm = mock[UserForm]
    val userService = mock[UserService]

    val controller = new LoginController(stubControllerComponents(), userService, userForm)

    TestObjects(stubControllerComponents(), userService, userForm, controller)
  }

  case class TestObjects(controllerComponent: ControllerComponents,
                         userService: UserService,
                         userForm: UserForm,
                         loginController: LoginController
                        )


}
