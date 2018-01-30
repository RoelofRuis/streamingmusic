package stream

import akka.Done
import akka.actor.Actor
import midi.{ControlChange, TimedMessage}
import util.JsonStorage

class StorageActor extends Actor {
  private var buffer: Seq[TimedMessage] = Seq()
  private var bufferCount: Int = 0

  override def receive: Receive = {
    case timedMessage @ TimedMessage(_, _) => buffer :+= timedMessage

    // TODO: Change hardcoded Left Pedal (Controller 67)
    case TimedMessage(_, ControlChange(67, 0)) =>
      JsonStorage.storeObjectInFile(buffer, s"out$bufferCount")
      buffer = Seq()
      bufferCount += 1
    case Done =>
      JsonStorage.storeObjectInFile(buffer, "out")
      context.stop(self)
  }

}
