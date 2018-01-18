package music.knowledge

import music.knowledge.Interpret.ChordQuality
import util.LookupTree

/**
  * Holds the mapping between lists of interval functions and chord names.
  */
object ChordBank {
  private val chordTree = LookupTree.build[IntervalFunction, ChordQuality](Map(
    // 3 note chords
    ChordQuality("Major") -> (Root :: Three :: Five :: Nil),
    ChordQuality("Minor") -> (Root :: FlatThree :: Five :: Nil),
    ChordQuality("Diminished") -> (Root :: FlatThree :: FlatFive :: Nil),
    ChordQuality("Augmented") -> (Root :: Three :: SharpFive :: Nil),
    ChordQuality("Sus 2") -> (Root :: Two :: Five :: Nil),
    ChordQuality("Sus 4") -> (Root :: Four :: Five :: Nil),

    // 4 note chords
    ChordQuality("Major 6") -> (Root :: Three :: Five :: Six :: Nil),
    ChordQuality("Minor 6") -> (Root :: FlatThree :: Five :: Six :: Nil),
    ChordQuality("Major 7") -> (Root :: Three :: Five :: Seven :: Nil),
    ChordQuality("Minor M7") -> (Root :: FlatThree :: Five :: Seven :: Nil),
    ChordQuality("Minor 7") -> (Root :: FlatThree :: Five :: FlatSeven :: Nil),
    ChordQuality("Dominant 7") -> (Root :: Three :: Five :: FlatSeven :: Nil),
    ChordQuality("Half Diminished 7") -> (Root :: FlatThree :: FlatFive :: FlatSeven :: Nil),
    ChordQuality("Diminished 7") -> (Root :: FlatThree :: FlatFive :: DiminishedSeven :: Nil),

    // 5 note chords
    ChordQuality("Dominant 9") -> (Root :: Three :: Five :: FlatSeven :: Nine :: Nil),
    ChordQuality("Major 9") -> (Root :: Three :: Five :: Seven :: Nine :: Nil),
  ))

  def find(l: List[IntervalFunction]): Option[ChordQuality] = chordTree.find(l.sorted)
}
