package midi

import akka.NotUsed
import akka.actor.ActorSystem
import akka.serial.stream.Serial
import akka.serial.{Parity, SerialSettings}
import akka.stream.scaladsl.{Keep, Sink, Source}
import akka.util.ByteString

import scala.concurrent.Future

object IO {
  private val SerialMidiSettings = SerialSettings(
    baud = 38400,
    characterSize = 8,
    twoStopBits = false,
    parity = Parity.None
  )

  def serialSource(port: String)(implicit actorSystem: ActorSystem): Source[ByteString, Future[Serial.Connection]] = {
    Source.empty.viaMat(Serial().open(port, SerialMidiSettings))(Keep.right[NotUsed, Future[Serial.Connection]])
  }

  def serialSink(port: String)(implicit actorSystem: ActorSystem): Sink[ByteString, Future[Serial.Connection]] = {
    Serial().open(port, SerialMidiSettings).to(Sink.ignore)
  }
}
