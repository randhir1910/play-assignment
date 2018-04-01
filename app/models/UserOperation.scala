package models

case class UserStoreData(firstName: String,
                         middleName: Option[String],
                         lastName: String,
                         username: String,
                         password: String,
                         rePassword: String,
                         mobile: String,
                         gender: String,
                         age: Int,
                         hobbies: String)

case class UserLoginData(username: String, password: String)

case class UserProfile(firstName: String,
                       middleName: Option[String],
                       lastName: String,
                       mobile: String,
                       gender: String,
                       age: Int,
                       hobbies: String)

case class AssignmentData(title: String,
                          description: String)

case class ForgetPassword(username: String,
                          newPassword: String,
                          confirmPassword: String)
