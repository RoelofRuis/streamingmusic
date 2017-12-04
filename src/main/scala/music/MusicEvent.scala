package music

sealed trait MusicEvent

case class NoteOn(note: Note, velocity: Byte) extends MusicEvent

case class NoteOff(note: Note) extends MusicEvent