package services

import org.scalatest.FunSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.reflect.ClassTag

class ModelTest[T: ClassTag] {
  def fakeApp: Application = {
    new GuiceApplicationBuilder().build()
  }

  lazy val app2doo: Application => T = Application.instanceCache[T]
  lazy val repository: T = app2doo(fakeApp)

}

class AssignmentServiceTest extends FunSuite {

  val repo = new ModelTest[AssignmentService]

  test("add assignment test") {
    val assignmentDetails = Assignment(1, "c++", "c++ program")
    val storeData = Await.result(repo.repository.createAssignment(assignmentDetails), Duration.Inf)
    assert(storeData === true)
  }

  test("delete assignment") {
    val deleteData = Await.result(repo.repository.deleteAssignment(1), Duration.Inf)
    assert(deleteData === true)

  }

  test("read assignment") {
    val assignmentList = Await.result(repo.repository.readAssignment, Duration.Inf)
    val data = List(Assignment(1, "c++", "c++ program"))
    assert(assignmentList === data)
  }
}
