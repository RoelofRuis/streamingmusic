import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{FileIO, Flow, Keep, Sink, Source}
import akka.util.ByteString
import com.typesafe.config.{Config, ConfigFactory}
import midi.{IO, MessageParser}
import stream.TimeGridFlow

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

object StreamMidi extends App {

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
        .via(Flow.fromGraph(new TimeGridFlow))
        .buffer(1, OverflowStrategy.backpressure)
        .zipWith(Source.tick(1.second, 1.second, Unit))((grid, _) => grid)
        .to(Sink.foreach(println))
    }
    .toMat(sink)(Keep.both).run()

  serialIn.onComplete(s => println(s"Serial In:$s\n"))
  serialOut.onComplete(s => println(s"Serial Out: $s\n"))

  def makeSink(conf: Config): Sink[ByteString, Future[Any]] = {
    val path = conf.getString("path")
    conf.getString("resource") match {
      case "file"   => FileIO.toPath(Paths.get(path))
      case "serial" => IO.serialSink(path)
      case r => throw new RuntimeException(s"unknown resource option $r")
    }
  }
}
