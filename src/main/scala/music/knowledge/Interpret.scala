package music.knowledge

import music.NoteName
import music.symbolic._
import types.{Interval, Note, NoteNumber, PitchClass}

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

  def intervalAsFunction: Interval => List[IntervalFunction] = {
    interval: Interval => {
      interval match {
        case MVec(0, 0) => Root :: Nil
        case MVec(1, -1) => FlatNine :: Nil
        case MVec(1, 0) => Two :: Nine :: Nil
        case MVec(2, -1) => FlatThree :: FlatTen :: Nil
        case MVec(2, 0) => Three :: Nil
        case MVec(3, 0) => Four :: Eleven :: Nil
        case MVec(3, 1) => SharpEleven :: FlatFive :: Nil
        case MVec(4, 0) => Five :: Nil
        case MVec(4, 1) => SharpFive :: Nil
        case MVec(5, -1) => FlatThirteen :: Nil
        case MVec(5, 0) => Six :: Thirteen :: Nil
        case MVec(6, -2) => DiminishedSeven :: Nil
        case MVec(6, -1) => FlatSeven :: Nil
        case MVec(6, 0) => Seven :: Nil
        case _ => Nil
      }
    }
  }

  case class ChordQuality(name: String) {
    override def toString: String = name
  }

  case class Chord(root: MVec, quality: ChordQuality) {
    override def toString: String = NoteName(root) + " " + quality.toString
  }

  // TODO: Rewrite as parse tree
  def functionsAsChordQuality: List[IntervalFunction] => Option[ChordQuality] = {
    funcs: List[IntervalFunction] => {
      funcs.sorted match {
        case Root :: Three :: Five :: Nil => Some(ChordQuality("Major"))
        case Root:: FlatThree:: Five :: Nil => Some(ChordQuality("Minor"))
        case Root :: Two :: Five :: Nil => Some(ChordQuality("Sus 2"))
        case Root :: Three :: Five :: Six :: Nil => Some(ChordQuality("Major 6"))
        case Root :: Three :: Five :: Seven :: Nil => Some(ChordQuality("Major 7"))
        case Root :: FlatThree :: Five :: FlatSeven :: Nil => Some(ChordQuality("Minor 7"))
        case Root :: Three :: Five :: FlatSeven :: Nil => Some(ChordQuality("Dom 7"))
        case _ => None
      }
    }
  }
}
