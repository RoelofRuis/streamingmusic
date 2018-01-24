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
    List(always(root), always(three), always(five)) -> ChordQuality(""),
    List(always(root), always(flatThree), always(five)) -> ChordQuality("m"),
    List(always(root), always(flatThree), always(flatFive)) -> ChordQuality("dim") ,
    List(always(root), always(three), always(sharpFive)) -> ChordQuality("#5"),
    List(always(root), always(two), always(five)) -> ChordQuality("sus 2"),
    List(always(root), always(four), always(five)) -> ChordQuality("sus 4"),

    // 4< note chords
    List(always(root), always(three), always(five), always(six)) -> ChordQuality("6"),
    List(always(root), always(flatThree), always(five), always(six)) -> ChordQuality("m6"),
    List(always(root), always(three), maybe(five), always(seven)) -> ChordQuality("Δ7"),
    List(always(root), always(flatThree), maybe(five), always(seven)) -> ChordQuality("m Δ7"),
    List(always(root), always(flatThree), maybe(five), always(flatSeven)) -> ChordQuality("m7"),
    List(always(root), always(three), maybe(five), always(flatSeven)) -> ChordQuality("7"),
    List(always(root), always(flatThree), always(flatFive), always(flatSeven)) -> ChordQuality("ø"),
    List(always(root), always(flatThree), always(flatFive), always(diminishedSeven)) -> ChordQuality("o"),
    List(always(root), always(three), always(five), always(nine)) -> ChordQuality("Δ/add9"),
    List(always(root), always(three), always(sharpFive), always(seven)) -> ChordQuality("#5/7"),

    // 5< note chords
    List(always(root), always(three), always(five), always(six), always(nine)) -> ChordQuality("6/9"),
    List(always(root), always(three), maybe(five), always(flatSeven), always(nine)) -> ChordQuality("9"),
    List(always(root), always(three), maybe(five), always(seven), always(nine)) -> ChordQuality("Δ9"),
    List(always(root), always(flatThree), maybe(five), always(flatSeven), always(nine)) -> ChordQuality("m9"),
    List(always(root), always(three), maybe(five), always(flatSeven), always(flatNine)) -> ChordQuality("7/b9"),
    List(always(root), always(three), maybe(five), always(flatSeven), always(flatTen)) -> ChordQuality("7/b10"),
    List(always(root), always(seven), always(nine), always(eleven)) -> ChordQuality("Δ11 (sus)"),

    // 6< note chords
    List(always(root), always(three), maybe(five), always(flatSeven), maybe(nine), always(eleven)) -> ChordQuality("11"),
    List(always(root), always(three), maybe(five), always(seven), maybe(nine), always(eleven)) -> ChordQuality("Δ11"),
    List(always(root), always(flatThree), maybe(five), always(flatSeven), maybe(nine), always(eleven)) -> ChordQuality("m11"),
    List(always(root), always(three), maybe(five), always(flatSeven), maybe(nine), always(sharpEleven)) -> ChordQuality("7/#11"),
    List(always(root), always(three), maybe(five), always(flatSeven), always(flatNine), always(sharpEleven)) -> ChordQuality("7/b9/#11"),
    List(always(root), always(three), maybe(five), always(flatSeven), always(flatTen), always(flatThirteen)) -> ChordQuality("7/b10/b13"),

    // 7< note chords
    List(always(root), always(three), maybe(five), always(flatSeven), maybe(nine), maybe(eleven), always(thirteen)) -> ChordQuality("13"),
    List(always(root), always(three), maybe(five), always(seven), maybe(nine), maybe(eleven), always(thirteen)) -> ChordQuality("Δ13"),
    List(always(root), always(flatThree), maybe(five), always(flatSeven), maybe(nine), maybe(eleven), always(thirteen)) -> ChordQuality("m13"),
    List(always(root), always(three), maybe(five), always(flatSeven), always(flatNine), maybe(eleven), always(thirteen)) -> ChordQuality("7/b9/13"),
    List(always(root), always(three), maybe(five), always(flatSeven), always(flatNine), maybe(eleven), always(flatThirteen)) -> ChordQuality("7/b9/b13"),
    List(always(root), always(three), always(flatFive), always(flatSeven), always(flatNine), maybe(eleven), always(flatThirteen)) -> ChordQuality("7/b5/b9/b13 (alt)"),
    List(always(root), always(three), maybe(five), always(flatSeven), maybe(nine), always(sharpEleven), always(thirteen)) -> ChordQuality("7/#11/13"),
  ))

  private val chordTree = LookupTree.build[IntervalFunction, ChordQuality](chordData)

  def find(l: List[IntervalFunction]): Option[ChordQuality] = chordTree.find(l.sorted)
}
