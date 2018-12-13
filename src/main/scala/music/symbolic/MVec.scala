package music.symbolic

case class MVec(step: Step, acc: Accidental) extends ScaleConversionTools with Comparable[MVec] {
  def +(other: MVec): MVec = {
    val newStep = this.step + other.step
    val newPc = toPitchClass(newStep)
    val actualPc = (toPitchClass(this.step) + this.acc) + (toPitchClass(other.step) + other.acc)

    val pcDiff = actualPc - newPc

    MVec(newStep % numSteps, pcDiff)
  }

  def -(other: MVec): MVec = {
    val newStep = this.step - other.step
    val newPc = toPitchClass(newStep)
    val actualPc = (toPitchClass(this.step) + this.acc) - (toPitchClass(other.step) + other.acc)

    val pcDiff = actualPc - newPc

    MVec(newStep % numSteps, pcDiff)
  }

  def rectify: MVec = {
    if (step >= 0) this
    else MVec(step + ((step / -numSteps) + 1) * numSteps, acc)
  }

  override def toString: String = s"[$step,$acc]"

  override def compareTo(other: MVec): Int = {
    val stepCompare = step.compare(other.step)
    if (stepCompare != 0) stepCompare
    else acc.compare(other.acc)
  }
}
