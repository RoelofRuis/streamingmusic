package music.symbolic

case class MVec(step: Step, accidental: Int = 0) {
  def +(other: MVec)(implicit ns: NotationSystem): MVec = {
    val newStep = this.step + other.step
    val newPc = ns.step2pc(newStep)
    val actualPc = (ns.step2pc(this.step) + this.accidental) + (ns.step2pc(other.step) + other.accidental)

    val pcDiff = (actualPc - newPc).n

    MVec(newStep % ns.numSteps, pcDiff)
  }

  def -(other: MVec)(implicit ns: NotationSystem): MVec = {
    val newStep = this.step - other.step
    val newPc = ns.step2pc(newStep)
    val actualPc = (ns.step2pc(this.step) + this.accidental) - (ns.step2pc(other.step) + other.accidental)

    val pcDiff = (actualPc - newPc).n

    MVec(newStep % ns.numSteps, pcDiff)
  }

  def rectify(implicit ns: NotationSystem): MVec = {
    if (step.n >= 0) this
    else MVec(step.n + ((step.n / -ns.numSteps) + 1) * ns.numSteps, accidental)
  }

  def asPitchClass(implicit ns: NotationSystem): PitchClass = ns.step2pc(step) + accidental

  override def toString: String = s"[${step.n},$accidental]"
}

object MVec {
  def interpret(pc: PitchClass)(implicit ns: NotationSystem): List[MVec] = {
    def tryMvec(pc: PitchClass, mod: Int): Option[MVec] = {
      ns.stepInScale(pc + mod).map(MVec(_, -mod))
    }
    Range.inclusive(-2, 2).flatMap(i => tryMvec(pc, i)).toList
  }

  def interpretRelative(pc: PitchClass, root: MVec)(implicit ns: NotationSystem): List[MVec] = interpret(pc).map(_ - root).map(_.rectify)

  def apply(step: Int, accidental: Int): MVec = MVec(Step(step), accidental)
}