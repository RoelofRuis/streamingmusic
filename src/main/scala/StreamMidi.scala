import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.serial.stream.Serial
import akka.serial.{Parity, SerialSettings}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Sink, Source}
import akka.util.ByteString
import graph.{BytestringSplitter, MessageParser, NoteAggregator}
import music.symbolic.{Interpretation, MVec}
import types.{PitchClass, Simultaneous}

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
    .map[Simultaneous[PitchClass]](_.map(_ % 12))
    .map[Simultaneous[Interpretation]](_.map(MVec(0).interpret(_)))
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
