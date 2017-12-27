package music.symbolic

import music.MidiNote

trait NotationSystem {
  val scale: Seq[Int]

  def numSteps: Int = scale.length

  def numPcs: Int = scale.sum

  def midi2pc(note: MidiNote): PitchClass = {
    PitchClass(note.noteNumber % numPcs)
  }

  def stepInScale(pc: PitchClass): Option[Int] = {
    def find(currentPc: Int, scaleSeq: Seq[Int]): Option[Int] = {
      val step = scaleSeq.scan(0)(_ + _).indexOf(currentPc)
      if (step == -1) None else Some(step)
    }
    if (pc.n >= 0) find(pc.n, scale)
    else find(-pc.n, scale.reverse).map(numSteps - _)
  }

  def step2pc(step: Step): PitchClass = {
    def loop(curStep: Int, total: Int, scaleSeq: Seq[Int]): Int = {
      if (curStep <= numSteps) scaleSeq.slice(0, curStep).sum + total
      else loop(curStep - numSteps, scaleSeq.sum, scaleSeq)
    }

    if (step.n >= 0) PitchClass(loop(step.n, 0, scale))
    else PitchClass(-loop(Math.abs(step.n), 0, scale.reverse))
  }
}

object StandardNotation extends NotationSystem {
  val scale: Seq[Int] = Seq(2, 2, 1, 2, 2, 2, 1)
}