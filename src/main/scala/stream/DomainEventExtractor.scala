package stream

import akka.stream.stage._
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import midi._
import music.Domain.{Control1, DomainEvent, Note}
import types.NoteNumber

class DomainEventExtractor extends GraphStage[FlowShape[TimedMessage, DomainEvent]] {
  val in: Inlet[TimedMessage] = Inlet[TimedMessage]("graph.DomainEventExtractor.in")
  val out: Outlet[DomainEvent] = Outlet[DomainEvent]("graph.DomainEventExtractor.out")

  val shape: FlowShape[TimedMessage, DomainEvent] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with StageLogging {
      var activeNotes: Map[NoteNumber, Long] = Map()

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          var nextEvent: Option[DomainEvent] = None

          grab[TimedMessage](in) match {
            case TimedMessage(startTime: Long, NoteOn(noteNumber, _)) =>
              activeNotes += noteNumber -> startTime

            case TimedMessage(endTime: Long, NoteOff(noteNumber)) =>
              nextEvent = Some(Note(noteNumber, activeNotes(noteNumber), endTime))

            // Right Pedal (64)
            case TimedMessage(timestamp: Long, ControlChange(64, value)) => // sustain

            // Left Pedal (67)
            case TimedMessage(timestamp: Long, ControlChange(67, 127)) => nextEvent = Some(Control1)

            case t @ TimedMessage(_, _) =>
              log.debug(s"Unforseen message: $t")
          }

          nextEvent match {
            case Some(event) => push(out, event)
            case None => pull(in)
          }
        }
      })

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          pull(in)
        }
      })
    }
}
