package object midi {
  sealed trait MidiMessage

  sealed trait NoteMessage extends MidiMessage

  case class NoteOn(noteNumber: Int, velocity: Int) extends NoteMessage
  case class NoteOff(noteNumber: Int) extends NoteMessage

  case class ControlChange(controller: Int, value: Int) extends MidiMessage

  object ControlChange {
    final val HIGH: Int = 127
    final val LOW: Int = 0
  }

  case class TimedMessage(timestamp: Long, message: MidiMessage)
}
