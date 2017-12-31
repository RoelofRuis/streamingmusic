import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.serial.stream.Serial
import akka.serial.{Parity, SerialSettings}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString
import graph.{BytestringSplitter, NoteAggregator, MessageParser, NoteInterpreter}

import scala.concurrent.ExecutionContext

object StreamMidi extends App {

  val argsList: Map[String, Any] = ArgParser.parseOptions(Map(), args.toList)

  implicit val system: ActorSystem = ActorSystem("midiserial")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val source = {
    if (argsList.contains("file")) {
      println(s"Reading from file ${argsList("file")}")
      openFileSource(argsList("file").toString)
    } else {
      println("Trying to open /dev/ttyAMA0")
      openSerialSource
    }
  }

  source
    .via(new BytestringSplitter())
    .via(new MessageParser())
    .via(new NoteAggregator())
    .via(new NoteInterpreter())
    .runWith(Sink.foreach(println))

  def openFileSource(path: String): Source[ByteString, _] = FileIO.fromPath(Paths.get(path))

  def openSerialSource: Source[ByteString, _] = {
    val settings: SerialSettings = SerialSettings(
      baud = 38400,
      characterSize = 8,
      twoStopBits = false,
      parity = Parity.None
    )

    Source.empty.via(Serial().open("/dev/ttyAMA0", settings))
  }
}

object ArgParser {
  def parseOptions(map: Map[String, Any], list: List[String]): Map[String, Any] = {
    list match {
      case Nil => map
      case "-f" :: value :: tail => parseOptions(map ++ Map("file" -> value), tail)
      case option :: tail => println("Unknown option " + option)
        sys.exit(0)
    }
  }
}