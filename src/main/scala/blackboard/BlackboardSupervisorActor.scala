package blackboard

import akka.Done
import akka.actor.{Actor, ActorRef, Props}
import music.Domain.Note

class BlackboardSupervisorActor extends Actor {

  val groupingActor: ActorRef = context.actorOf(Props[GroupingActor])

  override def receive: Receive = {
    case Done =>
      println("Blackboard received Done")
    case note @ Note(_, _, _) =>
      groupingActor ! note
  }

}

