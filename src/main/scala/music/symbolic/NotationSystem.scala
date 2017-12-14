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
}