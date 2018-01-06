package music.symbolic

import types.{Interval, MFunc, Note, NoteNumber, PitchClass}

object Interpret {

  def noteNumberAsPitchClass: NoteNumber => PitchClass = _ % 12

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
      Range.inclusive(-root.acc - 1, -root.acc + 2)
        .flatMap(mod => root.toStep(pitchClass + mod).map(MVec(_, -mod)))
        .to[List]
        .map(_ - root)
        .map(_.rectify)
        .filter(isValid)
  }

  def intervalAsFunction: Interval => List[MFunc] = {
    interval: Interval => {
      interval match {
        case MVec(0, 0) => MVec(1, 0) :: Nil
        case MVec(1, -1) => MVec(9, -1) :: Nil
        case MVec(1, 0) => MVec(2, 0) :: MVec(9, 0) :: Nil
        case MVec(2, -1) => MVec(3, -1) :: MVec(10, -1) :: Nil
        case MVec(2, 0) => MVec(3, 0) :: Nil
        case MVec(3, 0) => MVec(4, 0) :: MVec(11, 0) :: Nil
        case MVec(3, 1) => MVec(11, 1) :: MVec(5, -1) :: Nil
        case MVec(4, 0) => MVec(5, 0) :: Nil
        case MVec(4, 1) => MVec(5, 1) :: Nil
        case MVec(5, -1) => MVec(13, -1) :: Nil
        case MVec(5, 0) => MVec(6, 0) :: MVec(13, 0) :: Nil
        case MVec(6, -2) => MVec(7, -2) :: Nil
        case MVec(6, -1) => MVec(7, -1) :: Nil
        case MVec(6, 0) => MVec(7, 0) :: Nil
        case _ => Nil
      }
    }
  }

  // TODO: Rewrite as parse tree
  def functionsAsChord: List[MFunc] => Option[String] = {
    funcs: List[MFunc] => {
      funcs.sorted match {
        case MVec(1, 0) :: MVec(3, 0) :: MVec(5, 0):: Nil => Some("Major")
        case MVec(1, 0) :: MVec(3, -1) :: MVec(5, 0) :: Nil => Some("Minor")
        case MVec(1, 0) :: MVec(3, 0) :: MVec(5, 0) :: MVec(7, 0) :: Nil => Some("Major 7")
        case _ => None
      }
    }
  }
}
