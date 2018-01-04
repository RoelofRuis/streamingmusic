package music.symbolic

import types.{Interval, Note, NoteNumber, PitchClass}

object Interpret {

  def noteNumberAsPitchClass: NoteNumber => List[PitchClass] = _ % 12 :: Nil

  def pitchClassAsInterval(root: Note): PitchClass => List[Interval] = {
    def isValid(mVec: MVec): Boolean = {
      Set(
        MVec(0, 0),
        MVec(1, -1), MVec(1, 0),
        MVec(2, -1), MVec(2, 0),
        MVec(3, 0), MVec(3, 1),
        MVec(4, -1), MVec(4, 0), MVec(4, 1),
        MVec(5, -1), MVec(5, 0),
        MVec(6, -2), MVec(6, -1), MVec(6, 0)
      ).contains(mVec)
    }

    pitchClass: PitchClass =>
      Range.inclusive(-root.acc - 2, -root.acc + 1)
        .flatMap(mod => root.toStep(pitchClass + mod).map(MVec(_, -mod)))
        .to[List]
        .map(_ - root)
        .map(_.rectify)
        .filter(isValid)
  }

  case class MFunc(n: String)

  def intervalAsFunction: Interval => List[MFunc] = {
    interval: Interval => {
      interval match {
        case MVec(0, 0) => MFunc("r") :: Nil
        case MVec(1, -1) => MFunc("b9") :: Nil
        case MVec(1, 0) => MFunc("2") :: MFunc("9") :: Nil
        case MVec(2, -1) => MFunc("b3") :: MFunc("b10") :: Nil
        case MVec(2, 0) => MFunc("3") :: Nil
        case MVec(3, 0) => MFunc("4") :: MFunc("11") :: Nil
        case MVec(3, 1) => MFunc("#11") :: MFunc("b5") :: Nil
        case MVec(4, 0) => MFunc("5") :: Nil
        case MVec(4, 1) => MFunc("#5") :: Nil
        case MVec(5, -1) => MFunc("b13") :: Nil
        case MVec(5, 0) => MFunc("6") :: MFunc("13") :: Nil
        case MVec(6, -2) => MFunc("bb7") :: Nil
        case MVec(6, -1) => MFunc("7") :: Nil
        case MVec(6, 0) => MFunc("M7") :: Nil
        case _ => Nil
      }
    }
  }
}
