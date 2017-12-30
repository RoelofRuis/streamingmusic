package midi

sealed trait Message

sealed trait MusicMessage extends Message

case class NoteOn(noteNumber: Midi#NoteNumber, velocity: Int) extends MusicMessage

case class NoteOff(noteNumber: Midi#NoteNumber) extends MusicMessage