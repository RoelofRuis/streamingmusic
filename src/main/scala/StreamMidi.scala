import akka.Done
import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import blackboard.BlackboardActor
import stream.{BytestringSplitter, MessageParser, Sources}

import scala.concurrent.ExecutionContext

object StreamMidi extends App {

  val argsList: Map[String, Any] = ArgParser.parseOptions(Map(), args.toList)

  implicit val system: ActorSystem = ActorSystem("midiserial")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val source = {
    if (argsList.contains("file")) {
      println(s"Reading from file ${argsList("file")}")
      Sources.byteStringsFromFile(argsList("file").toString)
    } else {
      println("Trying to open /dev/ttyAMA0")
      Sources.serial
    }
  }

  val blackboard = system.actorOf(Props[BlackboardActor])

  source
    .via(new BytestringSplitter())
    .via(new MessageParser())
    .runWith(Sink.actorRef(blackboard, Done))
}
