package music.symbolic

case class NotationSystem(s: Seq[Int]) {
  def numNotes: Int = s.length

  def numSteps: Int = s.sum

  def steps(n: Int): Int = {
    s.slice(0, n).sum
  }
}