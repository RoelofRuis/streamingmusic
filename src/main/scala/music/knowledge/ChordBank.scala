package music.knowledge

import types.IntervalFunction
import music.knowledge.Interpret.ChordQuality
import util.LookupTree
import music.knowledge.{Intervals => I}

/**
  * Holds the mapping between lists of interval functions and chord names.
  */
object ChordBank {
  private val chordData = Map(
    // 3 note chords
    ChordQuality("Major") -> List(I.root, I.three, I.five),
    ChordQuality("Minor") -> List(I.root, I.flatThree, I.five),
    ChordQuality("Diminished") -> List(I.root, I.flatThree, I.flatFive),
    ChordQuality("Augmented") -> List(I.root, I.three, I.sharpFive),
    ChordQuality("Sus 2") -> List(I.root, I.two, I.five),
    ChordQuality("Sus 4") -> List(I.root, I.four, I.five),

    // 4 note chords
    ChordQuality("Major 6") -> List(I.root, I.three, I.five, I.six),
    ChordQuality("Minor 6") -> List(I.root, I.flatThree, I.five, I.six),
    ChordQuality("Major 7") -> List(I.root, I.three, I.five, I.seven),
    ChordQuality("Minor M7") -> List(I.root, I.flatThree, I.five, I.seven),
    ChordQuality("Minor 7") -> List(I.root, I.flatThree, I.five, I.flatSeven),
    ChordQuality("Dominant 7") -> List(I.root, I.three, I.five, I.flatSeven),
    ChordQuality("Half Diminished 7") -> List(I.root, I.flatThree, I.flatFive, I.flatSeven),
    ChordQuality("Diminished 7") -> List(I.root, I.flatThree, I.flatFive, I.diminishedSeven),

    // 5 note chords
    ChordQuality("Dominant 9") -> List(I.root, I.three, I.five, I.flatSeven, I.nine),
    ChordQuality("Major 9") -> List(I.root, I.three, I.five, I.seven, I.nine),

    ChordQuality("Dominant 7/b10") -> List(I.root, I.three, I.five, I.flatSeven, I.flatTen),
  )

  private val chordTree = LookupTree.build[IntervalFunction, ChordQuality](chordData)

  def find(l: List[IntervalFunction]): Option[ChordQuality] = chordTree.find(l.sorted)
}
