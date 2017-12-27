package music.symbolic

case class Step(n: Int) {
  def -(other: Step): Step = Step(n - other.n)

  def +(other: Step): Step = Step(n + other.n)

  def %(mod: Int): Step = Step(n % mod)
}
