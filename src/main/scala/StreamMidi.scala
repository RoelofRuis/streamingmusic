import akka.Done
import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import blackboard.BlackboardActor
import stream.{BytestringSplitter, MessageParser, Sources, StorageActor}

import scala.concurrent.ExecutionContext

object StreamMidi extends App {

  val argsList: Map[String, Any] = ArgParser.parseOptions(Map(), args.toList)

  implicit val system: ActorSystem = ActorSystem("midiserial")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val source = {
    if (argsList.contains("rawFile")) {
      println(s"Reading from file ${argsList("rawFile")}")
      Sources.byteStringsFromFile(argsList("rawFile").toString)
    } else {
      println("Trying to open /dev/ttyAMA0")
      Sources.serial
    }
  }

  val targetActor = {
    if (argsList.contains("storageFile")) {
      system.actorOf(Props[StorageActor])
    } else {
      system.actorOf(Props[BlackboardActor])
    }
  }

  source
    .via(new BytestringSplitter())
    .via(new MessageParser())
    .runWith(Sink.actorRef(targetActor, Done))
}
