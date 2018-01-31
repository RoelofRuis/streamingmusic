package stream

import akka.Done
import akka.actor.Actor
import midi.{ControlChange, JsonTimedMessages, TimedMessage}

class StorageActor extends Actor {
  private var buffer: Seq[TimedMessage] = Seq()
  private var bufferCount: Int = 0

  override def receive: Receive = {
    // TODO: Change hardcoded Left Pedal (Controller 67)
    case TimedMessage(_, ControlChange(67, 0)) =>
      JsonTimedMessages.writeAllToFile(buffer, s"out$bufferCount")
      buffer = Seq()
      bufferCount += 1
    case timedMessage @ TimedMessage(_, _) => buffer :+= timedMessage
    case Done =>
      JsonTimedMessages.writeAllToFile(buffer, "out")
      context.stop(self)
  }

}
