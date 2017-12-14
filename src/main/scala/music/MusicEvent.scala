package music

sealed trait MusicEvent

case class NoteOn(note: MidiNote, velocity: Byte) extends MusicEvent

case class NoteOff(note: MidiNote) extends MusicEvent

sealed trait MusicEntity

case class MidiNote(noteNumber: Byte) extends MusicEntity