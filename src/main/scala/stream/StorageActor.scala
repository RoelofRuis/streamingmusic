package stream

import akka.Done
import akka.actor.Actor
import midi.TimedMessage
import util.JsonStorage

class StorageActor extends Actor {
  private var buffer: Seq[TimedMessage] = Seq()

  override def receive: Receive = {
    case timedMessage @ TimedMessage(_, _) => buffer :+= timedMessage
    case Done =>
      JsonStorage.storeObjectInFile(buffer, "out")
      context.stop(self)
  }

}
