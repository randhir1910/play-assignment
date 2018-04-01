package services

import javax.inject.Inject

import models.{UserProfile}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


case class UserData(id: Int,
                    firstName: String,
                    middleName: Option[String],
                    lastName: String,
                    username: String,
                    password: String,
                    mobile: String,
                    gender: String,
                    age: Int,
                    hobbies: String,
                    isActive: Boolean = true,
                    isAdmin: Boolean = false)

class UserService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends UserRepositoryTable with UserFunctions {

  import profile.api._

  def addUser(user: UserData): Future[Boolean] =
    db.run(userQuery += user) map (_ > 0)

  def fetchByUsername(userName: String): Future[Option[UserData]] = {
    val queryResult = userQuery.filter(_.username.toLowerCase === userName.toLowerCase).result.headOption
    db.run(queryResult)
  }

  def validateUser(userName: String, password: String): Future[Option[UserData]] =
    db.run(userQuery.filter(user => user.username === userName && user.password === password).result.headOption)

  def isAdmin(userName: String): Future[Boolean] =
    db.run(userQuery.filter(user => user.username === userName).map(user => user.isAdmin).result.head)


  def isEnabled(userName: String): Future[Boolean] =
    db.run(userQuery.filter(user => user.username === userName).map(user => user.isActive).result.head)

  def updatePassword(userName: String, newPassword: String): Future[Boolean] =
    db.run(userQuery.filter(user => user.username === userName)
        .map(user => user.password).update(newPassword)).map(_ > 0)

  def updateProfile(username: String, updatedUserData: UserProfile): Future[Boolean] = {
    db.run(userQuery.filter(user => user.username === username).map(
      user => (user.firstName, user.middleName, user.lastName, user.mobile, user.gender, user.age, user.hobbies))
        .update(
          updatedUserData.firstName, updatedUserData.middleName, updatedUserData.lastName, updatedUserData.mobile
          , updatedUserData.gender, updatedUserData.age, updatedUserData.hobbies))
        .map(_ > 0)
  }

  def getAllUser: Future[List[UserData]] =
    db.run(userQuery.filter(user => user.isAdmin === false).to[List].result)


  def enableDisableUser(userName: String, isActive: Boolean): Future[Boolean] =
    db.run(userQuery.filter(user => user.username === userName).map(user => user.isActive).update(isActive)).map(_ > 0)
}


trait UserRepositoryTable extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val userQuery: TableQuery[UserTable] = TableQuery[UserTable]

  class UserTable(tag: Tag) extends Table[UserData](tag, "user") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def firstName: Rep[String] = column[String]("firstName")

    def middleName: Rep[Option[String]] = column[Option[String]]("middleName")

    def lastName: Rep[String] = column[String]("lastName")

    def username: Rep[String] = column[String]("username")

    def password: Rep[String] = column[String]("password")

    def mobile: Rep[String] = column[String]("mobile")

    def gender: Rep[String] = column[String]("gender")

    def age: Rep[Int] = column[Int]("age")

    def hobbies: Rep[String] = column[String]("hobbies")

    def isActive: Rep[Boolean] = column[Boolean]("isActive")

    def isAdmin: Rep[Boolean] = column[Boolean]("isAdmin")

    def * : ProvenShape[UserData] = (id, firstName, middleName, lastName, username, password, mobile, gender, age, hobbies, isActive, isAdmin) <> (UserData.tupled, UserData.unapply)
  }

}

trait UserFunctions {

  def addUser(user: UserData): Future[Boolean]

  def validateUser(username: String, password: String): Future[Option[UserData]]

  def isAdmin(username: String): Future[Boolean]

  def fetchByUsername(userName: String): Future[Option[UserData]]

  def isEnabled(username: String): Future[Boolean]

  def updatePassword(userName: String, newPassword: String): Future[Boolean]

  def updateProfile(username: String, updatedUserData: UserProfile): Future[Boolean]

  def enableDisableUser(username: String, isActive: Boolean): Future[Boolean]

  def getAllUser: Future[List[UserData]]

}
