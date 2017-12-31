package midi

import types.NoteNumber

sealed trait Message

sealed trait MusicMessage extends Message

case class NoteOn(noteNumber: NoteNumber, velocity: Int) extends MusicMessage
case class NoteOff(noteNumber: NoteNumber) extends MusicMessage

case class ControlChange(controller: Int, value: Int) extends Message
