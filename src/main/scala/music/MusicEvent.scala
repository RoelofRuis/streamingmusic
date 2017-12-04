package music

sealed abstract class MusicEvent

case class NoteOn(noteNumber: Byte, velocity: Byte) extends MusicEvent
case class NoteOff(noteNumber: Byte) extends MusicEvent