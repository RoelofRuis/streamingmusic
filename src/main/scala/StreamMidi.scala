import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.event.Logging
import akka.serial.stream.Serial
import akka.serial.{Parity, SerialSettings}
import akka.serial.stream.Serial.Connection
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Flow, Sink, Source}
import akka.util.ByteString
import graph.{BytestringSplitter, MusicEventParser}

import scala.concurrent.{ExecutionContext, Future}

object StreamMidi extends App {

  implicit val system: ActorSystem = ActorSystem("midiserial")
  val log = Logging(system.eventStream, "my.nice.string")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher


  val settings: SerialSettings = SerialSettings(
    baud = 38400,
    characterSize = 8,
    twoStopBits = false,
    parity = Parity.None
  )

  println("Trying to open /dev/ttyAMA0")
  val serial: Flow[ByteString, ByteString, Future[Connection]] = Serial().open("/dev/ttyAMA0", settings)

  val fileSource: Source[ByteString, _] = FileIO.fromPath(Paths.get("raw/sample_cmajorscale.dat"))

  Source.empty
    .via(serial)
    .via(new BytestringSplitter())
    .via(new MusicEventParser())
    .runWith(Sink.foreach(println))

}