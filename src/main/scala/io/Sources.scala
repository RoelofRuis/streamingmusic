package io

import akka.NotUsed
import akka.actor.ActorSystem
import akka.serial.stream.Serial
import akka.serial.{Parity, SerialSettings}
import akka.stream.scaladsl.Source
import akka.util.ByteString
import midi.TimedMessage
import stream.MessageParser

object Sources {
  def midiMessagesFromSerial(implicit actorSystem: ActorSystem): Source[TimedMessage, NotUsed] = {
    midiSerial("/dev/ttyAMA0")
      .mapConcat(_.toVector)
      .via(new MessageParser())
  }

  def midiSerial(port: String)(implicit actorSystem: ActorSystem): Source[ByteString, NotUsed] = {
    val settings: SerialSettings = SerialSettings(
      baud = 38400,
      characterSize = 8,
      twoStopBits = false,
      parity = Parity.None
    )

    Source.empty.via(Serial().open(port, settings))
  }
}
