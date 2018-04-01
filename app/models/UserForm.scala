package models

import play.api.data.Form
import play.api.data.Forms._


class UserForm {

  val registrationForm = Form(mapping(
    "firstName" -> text.verifying("please enter your first name", _.nonEmpty),
    "middleName" -> optional(text),
    "lastName" -> text.verifying("please enter your Last name", _.nonEmpty),
    "username" -> text.verifying("please enter your username", _.nonEmpty),
    "password" -> text.verifying("please enter your password", _.nonEmpty),
    "rePassword" -> text.verifying("please re enter password ", _.nonEmpty),
    "mobile" -> text.verifying("please enter your valid mobile number", _.toString.length == 10),
    "gender" -> text.verifying("please select your gender ", _.nonEmpty),
    "age" -> number(min = 18, max = 75),
    "hobbies" -> text.verifying("please select your hobby ", _.nonEmpty),
  )(UserStoreData.apply)(UserStoreData.unapply)
      verifying("Password and Confirm Password should be Equal ", user => user.password == user.rePassword)
  )

  val loginForm = Form(mapping(
    "username" -> text.verifying("enter your username", _.nonEmpty),
    "password" -> text.verifying("enter your password", _.nonEmpty)
  )(UserLoginData.apply)(UserLoginData.unapply))

  val profileForm = Form(mapping(
    "firstName" -> text.verifying("please enter your first name", _.nonEmpty),
    "middleName" -> optional(text),
    "lastName" -> text.verifying("please enter your Last name", _.nonEmpty),
    "mobile" -> text.verifying("please enter your valid mobile number", _.toString.length == 10),
    "gender" -> text.verifying("please select your gender ", _.nonEmpty),
    "age" -> number(min = 18, max = 75),
    "hobbies" -> text.verifying("please select your hobby ", _.nonEmpty)
  )(UserProfile.apply)(UserProfile.unapply))

  val assignmentForm = Form(mapping(
    "title" -> text.verifying("please enter title", _.nonEmpty),
    "description" -> text.verifying("please enter description", _.nonEmpty)
  )(AssignmentData.apply)(AssignmentData.unapply))

  val forgetPasswordForm = Form(mapping(
    "username" -> text.verifying("please enter your username", _.nonEmpty),
    "newPassword" -> text.verifying("please enter your password", _.nonEmpty),
    "confirmPassword" -> text.verifying("please re Enter your password", _.nonEmpty)
  )(ForgetPassword.apply)(ForgetPassword.unapply)
      verifying("Password and Confirm Password should be Equal ", user => user.newPassword == user.confirmPassword)
  )

}