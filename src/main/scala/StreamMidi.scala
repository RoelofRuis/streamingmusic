import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Sink}
import com.typesafe.config.{Config, ConfigFactory}
import io.Sources
import stream.MessageParser

import scala.concurrent.ExecutionContext

object StreamMidi extends App {

  implicit val system: ActorSystem = ActorSystem("midiserial")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val config: Config = ConfigFactory.load("application.conf")

  Sources.midiSerial("/dev/ttyAMA0")
    .alsoTo(FileIO.toPath(Paths.get("test.bin")))
    .mapConcat(_.toVector)
    .via(new MessageParser)
    .to(Sink.foreach(println))

}
