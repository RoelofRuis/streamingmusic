import music.symbolic.MVec

package object types {
  // TODO: These should not be here but incorporated into the music knowledge package

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