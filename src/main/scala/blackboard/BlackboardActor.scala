package blackboard

import akka.Done
import akka.actor.Actor
import midi.TimedMessage

class BlackboardActor extends Actor {

  override def receive: Receive = {
    case Done =>
      println("Blackboard received Done")
      context.stop(self)
    case TimedMessage(timestamp, message) => println(s"Blackboard received @$timestamp: ${message.toString}")
  }

}
