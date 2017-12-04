package music

sealed trait MusicEntity

case class Note(noteNumber: Byte) extends MusicEntity

case class Chord(notes: Seq[Note]) extends MusicEntity