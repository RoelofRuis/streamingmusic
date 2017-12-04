package util

object Format {

  def byteAsString(byte: Byte): String = {
    byte.toBinaryString.takeRight(8).formatted("%8s").replace(" ", "0")
  }

}
