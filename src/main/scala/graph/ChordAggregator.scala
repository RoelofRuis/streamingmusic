package graph

import akka.stream.stage._
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import midi.{ControlChange, Message, NoteOff, NoteOn}
import types.{NoteNumber, Simultaneous}

class ChordAggregator extends GraphStage[FlowShape[Message, Simultaneous[NoteNumber]]] {
  val in: Inlet[Message] = Inlet[Message]("graph.ChordAggregator.in")
  val out: Outlet[Simultaneous[NoteNumber]] = Outlet[Simultaneous[NoteNumber]]("graph.ChordAggregator.out")

  val shape: FlowShape[Message, Simultaneous[NoteNumber]] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with StageLogging {
      var sustainedNotes: Simultaneous[NoteNumber] = Set()
      var activeNotes: Simultaneous[NoteNumber] = Set()
      var sustainActive: Boolean = false

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          grab[Message](in) match {
            case message: NoteOn =>
              activeNotes += message.noteNumber
              sustainedNotes -= message.noteNumber

            case message: NoteOff =>
              if (sustainActive) sustainedNotes += message.noteNumber
              else activeNotes -= message.noteNumber

            // Pedal pressed - todo: move controller number to config
            case ControlChange(64, 127) =>
              sustainActive = true

            // Pedal released
            case ControlChange(64, 0) =>
              sustainActive = false
              activeNotes = activeNotes -- sustainedNotes
              sustainedNotes = Set()

            case ControlChange(controller, value) => log.info(s"Controller $controller changed to $value")
          }
          push(out, activeNotes)
        }
      })

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          pull(in)
        }
      })
    }
}
