import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink}
import akka.util.ByteString
import com.typesafe.config.{Config, ConfigFactory}
import midi.{IO, MessageParser}
import music.knowledge.Interpret
import music.knowledge.Interpret.Chord
import stream.TimeGridData.MusicEvent
import stream.TimeGridFlow
import util.Interpretation
import util.timed.TimeGrid

import scala.concurrent.{ExecutionContext, Future}

object StreamMidi extends App {
  import PrintTimeGrid._

  implicit val system: ActorSystem = ActorSystem("midiserial")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val config: Config = ConfigFactory.load("application.conf")

  val source = IO.serialSource(config.getString("midi.in"))
  val sink   = makeSink(config.getConfig("midi.out"))

  val (serialIn, serialOut) = source
    .alsoTo {
      Flow[ByteString]
        .mapConcat(_.toVector)
        .via(Flow.fromGraph(new MessageParser))
        .via(Flow.fromGraph(new TimeGridFlow(config.getInt("control.time-grid.controller-id"))))
//        .map(groupGrid)
        .map(_.map(analyseChord))
        .to(Sink.foreach(describe(_)(_.toString)))
    }
    .toMat(sink)(Keep.both).run()

  serialIn.onComplete(s => println(s"Serial In:$s\n"))
  serialOut.onComplete(s => println(s"Serial Out: $s\n"))

  def makeSink(conf: Config): Sink[ByteString, Future[Any]] = {
    val path = conf.getString("path")
    conf.getString("resource") match {
      case "file"   => FileIO.toPath(Paths.get(path))
      case "serial" => IO.serialSink(path)
      case "ignore" => Sink.ignore
      case r => throw new RuntimeException(s"unknown resource option $r")
    }
  }

  def analyseChord(notes: List[MusicEvent]): Interpretation[Chord] = {
    val pcs = Interpretation.allOf(
      notes
        .map(_.midiNoteNumber)
        .map(Interpret.noteNumberAsPitchClass)
    )

    Interpret.interpretOverRoots(pcs)
  }

  // TODO: some (pluggable) algorithm that returns the appropriate chord groups
  def groupGrid(grid: TimeGrid[List[MusicEvent]]): TimeGrid[List[MusicEvent]] = ???
}
