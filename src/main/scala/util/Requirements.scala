package util

import scala.reflect.ClassTag

/**
  * Defines requirements that might or might not have to be represent.
  *
  * `expand` resolves the requirements and returns the appropriate expanded map:
  * Map(
  *   List(always(1), always(2)) -> 'val1',
  *   List(maybe(2), always(3)) -> 'val2',
  * )
  * expands to:
  * Map(
  *   List(1, 2) -> 'val1'
  *   List(2, 3) -> 'val2',
  *   List(3) -> 'val2',
  * )
  *
  */
object Requirements {
  def always[A](a: A): Always[A] = Always(a)

  def maybe[A](a: A): Maybe[A] = Maybe(a)

  def expand[A : ClassTag, B](m: Map[List[Required[A]], B]): Map[List[A], B] = {
    m.foldLeft[Map[List[A], B]](Map.empty[List[A], B]) {
      case (aggregate, (list, value)) => aggregate ++ validLists(list).map(_ -> value)
    }
  }

  private def validLists[A : ClassTag](l: List[Required[A]]): List[List[A]] = {
    l match {
      case Nil => List(Nil)
      case Always(head: A) :: tail => validLists(tail).map(head :: _)
      case Maybe(head: A) :: tail => validLists[A](Always(head) :: tail) ++ validLists[A](tail)
    }
  }

  sealed trait Required[A]

  case class Maybe[A](a: A) extends Required[A]
  case class Always[A](a: A) extends Required[A]
}
