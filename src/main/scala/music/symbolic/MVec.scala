package music.symbolic

import types._

case class MVec(step: Step, acc: Accidental) extends ScaleConversionTools {
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

  def interpret(pc: PitchClass): Interpretation = {
    val interpretations = Range.inclusive(-acc - 1, -acc + 1)
      .flatMap(mod => toStep(pc + mod).map(MVec(_, -mod)))
      .to[Set]
      .map(_ - this)
      .map(_.rectify)

    Interpretation(this, interpretations)
  }

  override def toString: String = s"[$step,$acc]"
}
