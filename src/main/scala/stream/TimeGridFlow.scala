package stream

import akka.stream._
import akka.stream.stage._
import midi.{ControlChange, NoteOff, NoteOn, TimedMessage}
import stream.TimeGridData.MusicEvent
import util.timed.{SimultaneousTimeGridBuilder, TimeGrid}

class TimeGridFlow(controllerId: Int) extends GraphStage[FlowShape[TimedMessage, TimeGrid[List[MusicEvent]]]] {

  val in: Inlet[TimedMessage] = Inlet[TimedMessage]("graph.TimeGridFlow.in")
  val out: Outlet[TimeGrid[List[MusicEvent]]] = Outlet[TimeGrid[List[MusicEvent]]]("graph.TimeGridFlow.out")

  val shape: FlowShape[TimedMessage, TimeGrid[List[MusicEvent]]] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape: Shape) with StageLogging with InHandler with OutHandler {

      private var pulled = false
      private val builder = new SimultaneousTimeGridBuilder[MusicEvent, Int]

      override def preStart(): Unit = pull(in)
      override def onPull(): Unit = pulled = true

      override def onPush(): Unit = {
        val timedMessage = grab(in)
        timedMessage.message match {
          case NoteOn(noteNumber, _) => builder.start(timedMessage.timestamp, noteNumber, MusicEvent(noteNumber))
          case NoteOff(noteNumber) => builder.end(timedMessage.timestamp, noteNumber)
          case ControlChange(controller, 127) if controller == controllerId =>
            if (pulled) {
              pulled = false
              push(out, builder.grid())
              builder.reset()
            }
          case _ =>
        }

        pull(in)
      }


      setHandlers(in, out, this)
    }
}
