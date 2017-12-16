package music.symbolic

case class NotationSystem(s: Seq[Int]) {
  def numNotes: Int = s.length

  def numSteps: Int = s.sum

  def steps(n: Int): Int = {
    @annotation.tailrec
    def loop(n: Int, acc: Int): Int = {
      if (n <= numNotes) s.slice(0, n).sum + acc
      else loop(n - numNotes, s.sum)
    }
    loop(n, 0)
  }

  // TODO: Improve very naive implementation
  def notes(steps: Int): MVec = {
    val note = s.scan(0)(_ + _).find(_ == steps)
    if (note.isDefined) MVec(note.get)
    else MVec(note.get - 1, 1)
  }
}