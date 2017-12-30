package music.symbolic

import types._

case class MVec(step: Step, acc: Accidental = 0) {
  def +(other: MVec)(implicit ns: NotationSystem): MVec = {
    val newStep = this.step + other.step
    val newPc = ns.step2pc(newStep)
    val actualPc = (ns.step2pc(this.step) + this.acc) + (ns.step2pc(other.step) + other.acc)

    val pcDiff = actualPc - newPc

    MVec(newStep % ns.numSteps, pcDiff)
  }

  def -(other: MVec)(implicit ns: NotationSystem): MVec = {
    val newStep = this.step - other.step
    val newPc = ns.step2pc(newStep)
    val actualPc = (ns.step2pc(this.step) + this.acc) - (ns.step2pc(other.step) + other.acc)

    val pcDiff = actualPc - newPc

    MVec(newStep % ns.numSteps, pcDiff)
  }

  def rectify(implicit ns: NotationSystem): MVec = {
    if (step >= 0) this
    else MVec(step + ((step / -ns.numSteps) + 1) * ns.numSteps, acc)
  }

  def asPitchClass(implicit ns: NotationSystem): PitchClass = ns.step2pc(step) + acc

  def interpret(pc: PitchClass)(implicit ns: NotationSystem): OneOf[MVec] = {
    def tryMvec(pc: PitchClass, mod: Int): Option[MVec] = {
      ns.stepInScale(pc + mod).map(MVec(_, -mod))
    }

    Range.inclusive(-acc - 2, -acc + 2)
      .flatMap(i => tryMvec(pc, i))
      .toSet[MVec]
      .map(_ - this)
      .map(_.rectify)
  }

  override def toString: String = s"[$step,$acc]"
}
