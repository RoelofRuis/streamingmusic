package stream

import akka.stream._
import akka.stream.stage._
import midi.{NoteOff, NoteOn, TimedMessage}
import util.timed.{SimultaneousTimeGridBuilder, TimeGrid}

class TimeGridFlow extends GraphStage[FlowShape[TimedMessage, TimeGrid[List[NoteOn]]]] {

  val in: Inlet[TimedMessage] = Inlet[TimedMessage]("graph.TimeGridFlow.in")
  val out: Outlet[TimeGrid[List[NoteOn]]] = Outlet[TimeGrid[List[NoteOn]]]("graph.TimeGridFlow.out")

  val shape: FlowShape[TimedMessage, TimeGrid[List[NoteOn]]] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape: Shape) with StageLogging with InHandler with OutHandler {

      private val builder = new SimultaneousTimeGridBuilder[NoteOn, Int]

      override def preStart(): Unit = pull(in)
      override def onPull(): Unit = push(out, builder.grid())

      override def onPush(): Unit = {
        val timedMessage = grab(in)
        timedMessage.message match {
          case msg @ NoteOn(noteNumber, _) => builder.start(timedMessage.timestamp, noteNumber, msg)
          case NoteOff(noteNumber) => builder.end(timedMessage.timestamp, noteNumber)
          case _ =>
        }

        pull(in)
      }


      setHandlers(in, out, this)
    }
}