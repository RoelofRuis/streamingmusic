package music.symbolic

import types.{Interval, Note, NoteNumber, PitchClass}

object Interpret {

  def noteNumberAsPitchClass: NoteNumber => List[PitchClass] = _ % 12 :: Nil

  def pitchClassAsInterval(root: Note): PitchClass => List[Interval] = {
    def isValid(mVec: MVec): Boolean = {
      Set(MVec(0, 0), MVec(2, 0), MVec(4, 0), MVec(6, -1), MVec(6, 0)).contains(mVec)
    }

    pitchClass: PitchClass =>
      Range.inclusive(-root.acc - 1, -root.acc + 1)
        .flatMap(mod => root.toStep(pitchClass + mod).map(MVec(_, -mod)))
        .to[List]
        .map(_ - root)
        .map(_.rectify)
        .filter(isValid)
  }

  case class MusicFunction(n: String)

  def intervalAsFunction: Interval => List[MusicFunction] = {
    interval: Interval => {
      interval match {
        case MVec(0, 0) => MusicFunction("r") :: Nil
        case MVec(2, 0) => MusicFunction("3") :: Nil
        case MVec(4, 0) => MusicFunction("5") :: Nil
        case MVec(6, -1) => MusicFunction("7") :: Nil
        case MVec(6, 0) => MusicFunction("M7") :: Nil
        case _ => Nil
      }
    }
  }
}
