package io

import akka.NotUsed
import akka.actor.ActorSystem
import akka.serial.stream.Serial
import akka.serial.{Parity, SerialSettings}
import akka.stream.scaladsl.Source
import akka.util.ByteString

object Sources {
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
