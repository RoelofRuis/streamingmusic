package blackboard

import akka.Done
import akka.actor.Actor
import music.Domain.Note

class BlackboardSupervisorActor extends Actor {

  override def receive: Receive = {
    case Done =>
      println("Blackboard received Done")
    case note @ Note(_, _, _) =>
      println(note)
  }

}

