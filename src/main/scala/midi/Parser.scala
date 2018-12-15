package midi

import midi.Parser._

/**
  * @see https://learn.sparkfun.com/tutorials/midi-tutorial/all for info about messages and parsing.
  */
case class Parser private (
  status: Option[MidiByte],
  expectsBytes: Int,
  data: List[Data]
) {
  def next(in: Byte): (Parser, Option[MidiMessage]) = {
    in.asMidiByte match {
      case status: TwoByteStatus => to(status, 2)
      case d: Data if status.isDefined && expectsBytes > 1 => to(status.get, expectsBytes - 1, d :: data)
      case d: Data if status.isDefined && expectsBytes == 1 => emit(status.get, d :: data)
      case _ => (Parser(), None)
    }
  }

  private def nextMessage(status: MidiByte, data: List[Data]): Option[MidiMessage] = {
    status match {
      case NoteOnStatus(_) if data.head.byte != 0 => Some(NoteOn(data(1).byte, data.head.byte))
      case NoteOnStatus(_) if data.head.byte == 0 => Some(NoteOff(data(1).byte))
      case ControlChangeStatus(_) => Some(ControlChange(data(1).byte, data.head.byte))
      case _ => None
    }
  }

  private def to(status: MidiByte, expectsBytes: Int, data: List[Data] = List()): (Parser, Option[MidiMessage]) =
    (Parser(Some(status), expectsBytes, data), None)

  private def emit(status: MidiByte, data: List[Data]): (Parser, Option[MidiMessage]) = (Parser(), nextMessage(status, data))
}

object Parser {
  def apply(): Parser = Parser(None, 0, List())

  sealed trait MidiByte
  sealed trait TwoByteStatus extends MidiByte

  case class NoteOnStatus(channel: Int) extends TwoByteStatus
  case class ControlChangeStatus(channel: Int) extends TwoByteStatus

  case class Data(byte: Int) extends MidiByte
  case object Ignored extends MidiByte

  implicit class MidiByteOps(b: Byte) {
    private val StatusMask = 0x80.toByte

    private val NoteOn = 0x90.toByte
    private val ControlChange = 0xB0.toByte

    def asMidiByte: MidiByte = {
      if (isStatusByte) {
        msNybble match {
          case NoteOn => NoteOnStatus(lsNybble)
          case ControlChange => ControlChangeStatus(lsNybble)
          case _ => Ignored
        }
      }
      else Data(b)
    }

    private def msNybble: Int = b & 0xf0.toByte
    private def lsNybble: Int = b & 0x0f.toByte
    private def isStatusByte: Boolean = (b & StatusMask) != 0
  }
}