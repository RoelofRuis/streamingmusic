package music.knowledge

import types.IntervalFunction
import music.knowledge.Interpret.ChordQuality
import util.LookupTree
import music.knowledge.{Intervals => I}

/**
  * Holds the mapping between lists of interval functions and chord names.
  */
object ChordBank {
  private val chordTree = LookupTree.build[IntervalFunction, ChordQuality](Map(
    // 3 note chords
    ChordQuality("Major") -> (I.root :: I.three :: I.five :: Nil),
    ChordQuality("Minor") -> (I.root :: I.flatThree :: I.five :: Nil),
    ChordQuality("Diminished") -> (I.root :: I.flatThree :: I.flatFive :: Nil),
    ChordQuality("Augmented") -> (I.root :: I.three :: I.sharpFive :: Nil),
    ChordQuality("Sus 2") -> (I.root :: I.two :: I.five :: Nil),
    ChordQuality("Sus 4") -> (I.root :: I.four :: I.five :: Nil),

    // 4 note chords
    ChordQuality("Major 6") -> (I.root :: I.three :: I.five :: I.six :: Nil),
    ChordQuality("Minor 6") -> (I.root :: I.flatThree :: I.five :: I.six :: Nil),
    ChordQuality("Major 7") -> (I.root :: I.three :: I.five :: I.seven :: Nil),
    ChordQuality("Minor M7") -> (I.root :: I.flatThree :: I.five :: I.seven :: Nil),
    ChordQuality("Minor 7") -> (I.root :: I.flatThree :: I.five :: I.flatSeven :: Nil),
    ChordQuality("Dominant 7") -> (I.root :: I.three :: I.five :: I.flatSeven :: Nil),
    ChordQuality("Half Diminished 7") -> (I.root :: I.flatThree :: I.flatFive :: I.flatSeven :: Nil),
    ChordQuality("Diminished 7") -> (I.root :: I.flatThree :: I.flatFive :: I.diminishedSeven :: Nil),

    // 5 note chords
    ChordQuality("Dominant 9") -> (I.root :: I.three :: I.five :: I.flatSeven :: I.nine :: Nil),
    ChordQuality("Major 9") -> (I.root :: I.three :: I.five :: I.seven :: I.nine :: Nil),
  ))

  def find(l: List[IntervalFunction]): Option[ChordQuality] = chordTree.find(l.sorted)
}
