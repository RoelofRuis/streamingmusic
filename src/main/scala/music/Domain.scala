package music

package Domain {

  import types.NoteNumber

  sealed trait DomainEvent

  case class Note(note: NoteNumber, startTime: Long, endTime: Long) extends DomainEvent

  case class NoteGroup(notes: Seq[Note], startTime: Long, endTime: Long)

  case object Control1 extends DomainEvent

}
