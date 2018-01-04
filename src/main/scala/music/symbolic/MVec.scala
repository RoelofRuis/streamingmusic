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

  override def toString: String = s"[$step,$acc]"
}
