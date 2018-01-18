import music.symbolic.MVec

package object types {
  // MIDI
  type NoteNumber = Int

  // Symbolic Music
  type PitchClass = Int
  type Step = Int
  type Accidental = Int

  type Note = MVec
  type Interval = MVec
  type IntervalFunction = MVec
}