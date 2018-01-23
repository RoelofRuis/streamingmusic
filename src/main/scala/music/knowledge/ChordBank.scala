package music.knowledge

import music.knowledge.Interpret.ChordQuality
import music.knowledge.Intervals._
import types.IntervalFunction
import util.Requirements.{always, maybe}
import util.{LookupTree, Requirements}

/**
  * Holds the mapping between lists of interval functions and chord names.
  */
object ChordBank {
  private val chordData: Map[List[IntervalFunction], ChordQuality] = Requirements.expand(Map(
    // 3 note chords
    List(always(root), always(three), always(five)) -> ChordQuality("Major"),
    List(always(root), always(flatThree), always(five)) -> ChordQuality("Minor"),
    List(always(root), always(flatThree), always(flatFive)) -> ChordQuality("Diminished") ,
    List(always(root), always(three), always(sharpFive)) -> ChordQuality("Augmented"),
    List(always(root), always(two), always(five)) -> ChordQuality("Sus 2"),
    List(always(root), always(four), always(five)) -> ChordQuality("Sus 4"),

    // 4< note chords
    List(always(root), always(three), always(five), always(six)) -> ChordQuality("Major 6"),
    List(always(root), always(flatThree), always(five), always(six)) -> ChordQuality("Minor 6"),
    List(always(root), always(three), maybe(five), always(seven)) -> ChordQuality("Major 7"),
    List(always(root), always(flatThree), maybe(five), always(seven)) -> ChordQuality("Minor M7"),
    List(always(root), always(flatThree), maybe(five), always(flatSeven)) -> ChordQuality("Minor 7"),
    List(always(root), always(three), maybe(five), always(flatSeven)) -> ChordQuality("Dominant 7"),
    List(always(root), always(flatThree), always(flatFive), always(flatSeven)) -> ChordQuality("Half Diminished 7"),
    List(always(root), always(flatThree), always(flatFive), always(diminishedSeven)) -> ChordQuality("Diminished 7"),
    List(always(root), always(three), always(five), always(nine)) -> ChordQuality("Major add 9"),

    // 5< note chords
    List(always(root), always(three), always(five), always(six), always(nine)) -> ChordQuality("Major 6 9"),
    List(always(root), always(three), maybe(five), always(flatSeven), always(nine)) -> ChordQuality("Dominant 9"),
    List(always(root), always(three), maybe(five), always(flatSeven), always(flatNine)) -> ChordQuality("Dominant b9"),
    List(always(root), always(three), maybe(five), always(seven), always(nine)) -> ChordQuality("Major 9"),
    List(always(root), always(three), maybe(five), always(flatSeven), always(flatTen)) -> ChordQuality("Dominant b10"),

    // 6< note chords
    List(always(root), always(three), maybe(five), always(seven), maybe(nine), always(eleven)) -> ChordQuality("Major 11"),
    List(always(root), always(three), maybe(five), always(flatSeven), maybe(nine), always(eleven)) -> ChordQuality("Dominant 11"),
    List(always(root), always(three), maybe(five), always(flatSeven), maybe(nine), always(sharpEleven)) -> ChordQuality("Dominant #11"),
  ))

  private val chordTree = LookupTree.build[IntervalFunction, ChordQuality](chordData)

  def find(l: List[IntervalFunction]): Option[ChordQuality] = chordTree.find(l.sorted)
}
