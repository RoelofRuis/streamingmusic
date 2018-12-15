package music.symbolic

// TODO: rename and use this new representation
case class PitchClasss(value: Int) {
  assert(value >= 0 && value < 12, "Pitch class should be [0 11]")
}

object PitchClasss {
  def fromNoteNumber(noteNumber: Int): PitchClasss = PitchClasss(noteNumber % 12)
}