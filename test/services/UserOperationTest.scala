package services

import models.UserProfile
import org.scalatest.FunSuite

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class UserOperationTest extends FunSuite {

  val repo = new ModelTest[UserService]

  test("add user test") {
    val admin = UserData(1, "admin", Some("kk"), "istrator", "admin", "admin@123", "9953188803", "male", 23, "cricket", true, true)
    val user = UserData(1, "randhir", Some("kk"), "kumar", "randhir", "12345", "9953188803", "male", 23, "cricket", true, false)
    val addUser = Await.result(repo.repository.addUser(user), Duration.Inf)
    val addAdmin = Await.result(repo.repository.addUser(admin), Duration.Inf)
    assert(addUser === true)
    assert(addAdmin === true)

  }

  test("fetch by username") {

    val foundData = Await.result(repo.repository.fetchByUsername("randhir"), Duration.Inf)
    val notFountData = Await.result(repo.repository.fetchByUsername("xyz123"), Duration.Inf)
    val expectedData = UserData(1, "randhir", Some("kk"), "kumar", "randhir", "12345", "9953188803", "male", 23, "cricket", true, false)
    assert(foundData === Some(expectedData))
    assert(notFountData === None)
  }

  test("validate user") {
    val validateUser = Await.result(repo.repository.validateUser("randhir", "12345"), Duration.Inf)
    val notAvailableUser = Await.result(repo.repository.validateUser("randhirrr", "1234995"), Duration.Inf)
    val expectedData = UserData(1, "randhir", Some("kk"), "kumar", "randhir", "12345", "9953188803", "male", 23, "cricket", true, false)
    assert(validateUser === Some(expectedData))
    assert(notAvailableUser === None)
  }


  test("admin check ") {
    val isAdmin = Await.result(repo.repository.isAdmin("admin"), Duration.Inf)
    val notAdmin = Await.result(repo.repository.isAdmin("randhir"), Duration.Inf)
    assert(isAdmin === true)
    assert(notAdmin === false)
  }

  test("user enable or disable check") {
    val isEnabled = Await.result(repo.repository.isEnabled("randhir"), Duration.Inf)
    assert(isEnabled === true)
  }


  test("update password") {

    val updatePassword = Await.result(repo.repository.updatePassword("randhir", "123456"), Duration.Inf)
    val failUpdatePassword = Await.result(repo.repository.updatePassword("ravi", "123456876"), Duration.Inf)
    assert(updatePassword === true)
    assert(failUpdatePassword === false)
  }

  test("get all users") {

    val user = UserData(1, "randhir", Some("kk"), "kumar", "randhir", "123456", "9953188803", "male", 23, "cricket", true, false)
    val users = Await.result(repo.repository.getAllUser, Duration.Inf)
    assert(users === List(user))
  }
  test("enable or disable users") {

    val isEnable = Await.result(repo.repository.enableDisableUser("ravi", false), Duration.Inf)
    val isEnable1 = Await.result(repo.repository.enableDisableUser("randhir", false), Duration.Inf)
    assert(isEnable === false)
    assert(isEnable1 === true)

  }

  test("update user") {
    val userProfile = UserProfile("randhir", Some("kk"), "singh", "9953188803", "male", 24, "net")
    val updateUser = Await.result(repo.repository.updateProfile("randhir", userProfile), Duration.Inf)
    assert(updateUser === true)
  }
}
