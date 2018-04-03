package services

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.lifted.ProvenShape

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class Assignment(id: Int,
                      title: String,
                      description: String)

trait AssignmentOperation {

  def createAssignment(assignment: Assignment): Future[Boolean]

  def deleteAssignment(id: Int): Future[Boolean]

  def readAssignment: Future[List[Assignment]]
}

class AssignmentService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends AssignmentRepositoryTable with AssignmentOperation {

  import profile.api._

  override def createAssignment(assignment: Assignment): Future[Boolean] =
    db.run(assignmentQuery += assignment) map (_ > 0)

  override def deleteAssignment(id: Int): Future[Boolean] =
    db.run(assignmentQuery.filter(assignment => assignment.id === id).delete).map(_ > 0)

  override def readAssignment: Future[List[Assignment]] =
    db.run(assignmentQuery.to[List].result)

}

trait AssignmentRepositoryTable extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  val assignmentQuery: TableQuery[AssignmentTable] = TableQuery[AssignmentTable]

  class AssignmentTable(tag: Tag) extends Table[Assignment](tag, "assignment") {

    def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def title: Rep[String] = column[String]("title")

    def description: Rep[String] = column[String]("description")

    // scalastyle:off
    def * : ProvenShape[Assignment] = (id, title, description) <> (Assignment.tupled, Assignment.unapply)
  }

}
