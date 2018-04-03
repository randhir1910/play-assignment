package models

import constant.Const
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

import scala.util.matching.Regex


class UserForm {

  val allNumbers: Regex = """\d*""".r
  val allLetters: Regex = """[A-Za-z]*""".r

  val mobileNumberCheckConstraint: Constraint[String] = Constraint("constraints.checkMobileNumber")({
    mobileNo =>
      if (mobileNo.length == 10) {
        mobileNo match {
          case allNumbers() => Valid
        }
      }
      else {
        Invalid(Seq(ValidationError("please enter valid mobile number")))
      }
  })

  val registrationForm: Form[UserStoreData] = Form(mapping(
    "firstName" -> text.verifying("please enter your first name", firstName => firstName.nonEmpty),
    "middleName" -> optional(text),
    "lastName" -> text.verifying("please enter your Last name", lastName => lastName.nonEmpty),
    "username" -> text.verifying("please enter your username", username => username.nonEmpty),
    "password" -> text.verifying("please enter your password", password => password.nonEmpty),
    "rePassword" -> text.verifying("please re enter password ", rePassword => rePassword.nonEmpty),
    "mobile" -> nonEmptyText.verifying(mobileNumberCheckConstraint),
    "gender" -> text.verifying("please select your gender ", gender => gender.nonEmpty),
    "age" -> number(min = Const.eightTeen, max = Const.seventyFive),
    "hobbies" -> text.verifying("please select your hobby ", hobbies => hobbies.nonEmpty)
  )(UserStoreData.apply)(UserStoreData.unapply)
      verifying("Password and Confirm Password should be Equal ", user => user.password == user.rePassword)
  )

  val loginForm: Form[UserLoginData] = Form(mapping(
    "username" -> text.verifying("enter your username", _.nonEmpty),
    "password" -> text.verifying("enter your password", _.nonEmpty)
  )(UserLoginData.apply)(UserLoginData.unapply))

  val profileForm: Form[UserProfile] = Form(mapping(
    "firstName" -> text.verifying("please enter your first name", _.nonEmpty),
    "middleName" -> optional(text),
    "lastName" -> text.verifying("please enter your Last name", _.nonEmpty),
    "mobile" -> nonEmptyText.verifying(mobileNumberCheckConstraint),
    "gender" -> text.verifying("please select your gender ", _.nonEmpty),
    "age" -> number(min = Const.eightTeen, max = Const.seventyFive),
    "hobbies" -> text.verifying("please select your hobby ", _.nonEmpty)
  )(UserProfile.apply)(UserProfile.unapply))

  val assignmentForm: Form[AssignmentData] = Form(mapping(
    "title" -> text.verifying("please enter title", _.nonEmpty),
    "description" -> text.verifying("please enter description", _.nonEmpty)
  )(AssignmentData.apply)(AssignmentData.unapply))

  val forgetPasswordForm: Form[ForgetPassword] = Form(mapping(
    "username" -> text.verifying("please enter your username", _.nonEmpty),
    "newPassword" -> text.verifying("please enter your password", _.nonEmpty),
    "confirmPassword" -> text.verifying("please re Enter your password", _.nonEmpty)
  )(ForgetPassword.apply)(ForgetPassword.unapply)
      verifying("Password and Confirm Password should be Equal ", user => user.newPassword == user.confirmPassword)
  )
}
