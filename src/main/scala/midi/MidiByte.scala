package midi

sealed trait MidiByte

sealed trait TwoByteStatus extends MidiByte

case class NoteOnStatus(channel: Int) extends TwoByteStatus

case class Data(byte: Int) extends MidiByte

case class Ignored() extends MidiByte

object MidiByte {
  private val NoteOn = 0x90.toByte

  def apply(byte: Byte): MidiByte = {
    if (isStatusByte(byte)) {
      MSNybble(byte) match {
        case NoteOn => NoteOnStatus(LSNybble(byte))
        case _ => Ignored()
      }
    } else {
      Data(byte)
    }
  }

  private def MSNybble(b: Byte): Int = b & 0xf0.toByte

  private def LSNybble(b: Byte): Int = b & 0x0f.toByte

  private def isStatusByte(b: Byte): Boolean = (b & 0x80.toByte) != 0
}
