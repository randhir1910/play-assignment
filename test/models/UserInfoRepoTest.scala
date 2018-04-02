//package models
//
//import akka.Done
//import org.specs2.mutable.Specification
//import play.api.Application
//import play.api.inject.guice.GuiceApplicationBuilder
//
//import scala.concurrent.Await
//import scala.concurrent.duration.Duration
//import scala.reflect.ClassTag
//
//
//class ModelsTest[T: ClassTag] {
//
//  def fakeApp: Application = {
//    new GuiceApplicationBuilder()
//        .build()
//  }
//
//  lazy val app2dao = Application.instanceCache[T]
//  lazy val repository: T = app2dao(fakeApp)
//
//}
//
//class UserInfoRepoTest extends Specification {
//
////  val repo = new ModelsTest[UserInfoRepo]
////
////  "user info repository" should {
////    "store associate " in {
////      val user = repo.repository.UserInfo("randhir", "kumar", "randhir1910@gmail.com")
////      val storeResult = Await.result(repo.repository.store(user), Duration.Inf)
////      storeResult must equalTo(Done)
////    }
////  }
//}
