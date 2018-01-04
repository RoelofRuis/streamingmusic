package util

/**
  * Models an interpretation of some phenomenon as a disjunction of conjunctions.
  *
  * For example the following phenomenon:
  * ((A & B) | (C & D))
  * That can be interpreted as "either 'A and B' or 'C and D'"
  *
  * Combinators exist on the Interpretation class to transition from one interpretation to another.
  */
object Interpretation {

  private type OneOf[A] = List[A]
  private type AllOf[A] = List[A]

  /* Creators */
  def empty[A]: Interpretation[A] = Interpretation(Nil)

  def only[A](a: A): Interpretation[A] = Interpretation((a :: Nil) :: Nil)

  def oneOf[A](l: List[A]): Interpretation[A] = Interpretation(l.map((a: A) => a :: Nil))

  def allOf[A](l: List[A]): Interpretation[A] = Interpretation(l :: Nil)

  case class Interpretation[A](data: OneOf[AllOf[A]]) {

    private def combineLists[T](l1: List[List[T]], l2: List[T]): List[List[T]] = {
      if (l2 == Nil) l1
      else l2.flatMap((elem: T) => l1.map((list: List[T]) => elem :: list))
    }

    private def resolveAllOf[B](a: AllOf[A])(f: A => List[B]): Interpretation[B] = {
      Interpretation(a.map(f).foldLeft(List(List[B]()))((res, elem) => combineLists[B](res, elem)))
    }

    def add(other: Interpretation[A]) = Interpretation(data ::: other.data)

    def map[B](f: A => List[B]): Interpretation[B] = {
      data.map((all: AllOf[A]) => resolveAllOf[B](all)(f)).reduce(_.add(_))
    }
  }
}
