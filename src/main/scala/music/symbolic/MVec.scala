package music.symbolic

case class MVec(n: Int, a: Int = 0) {
  def +(other: MVec)(implicit ns: NotationSystem): MVec = {
    val newN = this.n + other.n
    val newNSteps = ns.steps(newN)
    val actualSteps = ns.steps(this.n) + ns.steps(other.n) + this.a + other.a

    MVec(newN % ns.numNotes, actualSteps - newNSteps)
  }
}