package music.knowledge

import music.NoteName
import music.symbolic._
import util.Interpretation

/**
  * Different kinds of functions that map between interpretations.
  *
  * @deprecated Functions in this file should be cleanly separated as individual pieces of knowledge.
  */

object Interpret {

  type NoteNumber = Int

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

  case class ChordQuality(name: String) {
    override def toString: String = name
  }

  case class Chord(root: MVec, quality: ChordQuality) {
    override def toString: String = NoteName(root) + " " + quality.toString
  }

  // TODO: rewrite to factor out inner flat map and strict types
  def interpretOverRoots(i: Interpretation[PitchClass]): Interpretation[Chord] = {
    val roots = Seq(
      MVec(0, 0),
      MVec(0, 1),
      MVec(1, -1),
      MVec(1, 0),
      MVec(1, 1),
      MVec(2, -1),
      MVec(2, 0),
      MVec(3, 0),
      MVec(3, 1),
      MVec(4, -1),
      MVec(4, 0),
      MVec(4, 1),
      MVec(5, -1),
      MVec(5, 0),
      MVec(5, 1),
      MVec(6, -1),
      MVec(6, 0)
    )

    val chords = roots.flatMap {
      root =>
        i.distinctElements
          .expand(Interpret.pitchClassAsInterval(root))
          .expand(Intervals.asFunction)
          .mapAll(ChordBank.find)
          .data.flatten.map(Chord(root, _))
    }

    Interpretation.oneOf(chords.toList)
  }
}
