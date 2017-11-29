case class MidiByte(byte: Byte) {

  def isStatusByte: Boolean  = {
    (byte & 0x80.toByte) != 0
  }

  def isSystemRealtime: Boolean = {
    byte >= 0xF8.toByte
  }

  def isNoteOn: Boolean = {
    byte == 0x90.toByte
  }

  override def toString: String = {
    Format.byteAsString(byte)
  }

}
