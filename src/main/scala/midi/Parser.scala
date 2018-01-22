package midi

/**
  * @see https://learn.sparkfun.com/tutorials/midi-tutorial/all for info about messages and parsing.
  */
case class Parser(
                   status: Option[MidiByte] = None,
                   expectsBytes: Int = 0,
                   data: List[Data] = List()
                 ) {
  def nextState(in: MidiByte): (Parser, Option[Message]) = {
    in match {
      case s: TwoByteStatus => (Parser(Some(s), 2), None)
      case d: Data if status.isDefined && expectsBytes > 1 => (Parser(status, expectsBytes - 1, d :: data), None)
      case d: Data if status.isDefined && expectsBytes == 1 => (Parser(), nextMessage(status.get, d :: data))
      case _ => (Parser(), None)
    }
  }

  private def nextMessage(status: MidiByte, data: List[Data]): Option[Message] = {
    status match {
      case NoteOnStatus(_) =>
        if (data.head.byte != 0) Some(NoteOn(data(1).byte, data.head.byte))
        else Some(NoteOff(data(1).byte))
      case ControlChangeStatus(_) => Some(ControlChange(data(1).byte, data.head.byte))
      case _ => None
    }
  }
}
