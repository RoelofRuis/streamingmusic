package stream

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.serial.stream.Serial
import akka.serial.{Parity, SerialSettings}
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString

object Sources {
  /** @deprecated Has to be removed in favor of reading the json message format including the time */
  def byteStringsFromFile(path: String): Source[ByteString, _] = FileIO.fromPath(Paths.get(path))

  def serial(implicit actorSystem: ActorSystem): Source[ByteString, _] = {
    val settings: SerialSettings = SerialSettings(
      baud = 38400,
      characterSize = 8,
      twoStopBits = false,
      parity = Parity.None
    )

    Source.empty.via(Serial().open("/dev/ttyAMA0", settings))
  }
}
