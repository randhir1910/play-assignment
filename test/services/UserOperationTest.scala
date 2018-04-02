package services

import org.scalatest.FunSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder

import scala.reflect.ClassTag

class ModelTest[T: ClassTag] {
  def fakeApp: Application = {
    new GuiceApplicationBuilder().build()
  }

  lazy val app2doo: Application => T = Application.instanceCache[T]
  lazy val repository: T = app2doo(fakeApp)

}

class UserOperationTest extends FunSuite {

  val repo = new ModelTest[UserService]

}
