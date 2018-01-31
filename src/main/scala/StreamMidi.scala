import akka.Done
import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import blackboard.BlackboardActor
import stream.{Sources, StorageActor}

import scala.concurrent.ExecutionContext

object StreamMidi extends App {

  val argsList: Map[String, Any] = ArgParser.parseOptions(Map(), args.toList)

  implicit val system: ActorSystem = ActorSystem("midiserial")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  val source = {
    if (argsList.contains("dataFile")) {
      println(s"Reading from file ${argsList("dataFile")}")
      Sources.midiMessagesFromFile(argsList("dataFile").toString)
    } else {
      println("Trying to open /dev/ttyAMA0")
      Sources.midiMessagesFromSerial
    }
  }

  val targetActor = {
    if (argsList.contains("store") && argsList("store") == true) {
      system.actorOf(Props[StorageActor])
    } else {
      system.actorOf(Props[BlackboardActor])
    }
  }

  source.runWith(Sink.actorRef(targetActor, Done))
}
