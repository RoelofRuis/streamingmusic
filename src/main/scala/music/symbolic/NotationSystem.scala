package music.symbolic

import midi.Midi

trait NotationSystem {
  val scale: Seq[Int]

  type PitchClass = Int
  type Step = Int
  type Accidental = Int

  def numSteps: Int = scale.length

  def numPcs: Int = scale.sum

  def midi2pc(note: Midi#NoteNumber): PitchClass = {
    note % numPcs
  }

  def stepInScale(pc: PitchClass): Option[Step] = {
    def find(currentPc: Int, scaleSeq: Seq[Int]): Option[Int] = {
      val step = scaleSeq.scan(0)(_ + _).indexOf(currentPc)
      if (step == -1) None else Some(step)
    }
    if (pc >= 0) find(pc, scale)
    else find(-pc, scale.reverse).map(numSteps - _)
  }

  def step2pc(step: Step): PitchClass = {
    def loop(curStep: Int, total: Int, scaleSeq: Seq[Int]): Int = {
      if (curStep <= numSteps) scaleSeq.slice(0, curStep).sum + total
      else loop(curStep - numSteps, scaleSeq.sum, scaleSeq)
    }

    if (step >= 0) loop(step, 0, scale)
    else -loop(Math.abs(step), 0, scale.reverse)
  }
}

object StandardNotation extends NotationSystem {
  val scale: Seq[Int] = Seq(2, 2, 1, 2, 2, 2, 1)
}