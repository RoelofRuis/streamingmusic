package stream

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.serial.stream.Serial
import akka.serial.{Parity, SerialSettings}
import akka.stream.javadsl.Framing
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.ByteString
import midi.{JsonTimedMessages, TimedMessage}

object Sources {
  def midiMessagesFromFile(path: String): Source[TimedMessage, _] = {
    linesFromFile("data/IV-V-I_Cmaj.json")
      .map(JsonTimedMessages.deserializeOne)
  }

  def linesFromFile(path: String): Source[String, _] = {
    FileIO.fromPath(Paths.get(path))
      .via(Framing.delimiter(ByteString("\n"), maximumFrameLength = 1024))
      .map(_.utf8String)
  }

  def midiMessagesFromSerial(implicit actorSystem: ActorSystem): Source[TimedMessage, _] = {
    serial
      .via(new BytestringSplitter())
      .via(new MessageParser())
  }

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
