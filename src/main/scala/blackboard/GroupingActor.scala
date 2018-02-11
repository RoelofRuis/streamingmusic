package blackboard

import akka.actor.Actor
import music.Domain.{Note, NoteGroup}

import scala.collection.mutable.Map

class GroupingActor extends Actor {

  private var lastIndex: Int = 0
  private var groups: Map[Int, NoteGroup] = Map()

  override def receive: Receive = {
    case currNote @ Note(note, noteStartTime, noteEndTime) =>
      val matchingGroups = groups.filter(group => Math.abs(group._2.startTime - noteStartTime) < 50)

      if (matchingGroups.isEmpty) groups += nextIndex -> NoteGroup(Seq(currNote), noteStartTime, noteEndTime)
      if (matchingGroups.size == 1) {
        val index = matchingGroups.head._1
        groups(index) = NoteGroup(matchingGroups.head._2.notes :+ currNote, noteStartTime, noteEndTime)
      }
  }

  private def nextIndex: Int = {
    lastIndex += 1
    lastIndex
  }

}

