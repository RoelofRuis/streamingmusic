package stream

import akka.stream.stage._
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import midi.{ControlChange, Message, NoteOff, NoteOn}
import types.NoteNumber
import util.Interpretation

/**
  * @deprecated Aggregation should not be done through a graph stage.
  */
class NoteAggregator extends GraphStage[FlowShape[Message, Interpretation.Interpretation[NoteNumber]]] {
  val in: Inlet[Message] = Inlet[Message]("graph.ChordAggregator.in")
  val out: Outlet[Interpretation.Interpretation[NoteNumber]] = Outlet[Interpretation.Interpretation[NoteNumber]]("graph.ChordAggregator.out")

  val shape: FlowShape[Message, Interpretation.Interpretation[NoteNumber]] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with StageLogging {
      var sustainedNotes: Set[NoteNumber] = Set()
      var activeNotes: Set[NoteNumber] = Set()
      var sustainActive: Boolean = false

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          var newActiveNotes: Set[NoteNumber] = activeNotes

          grab[Message](in) match {
            case message: NoteOn =>
              newActiveNotes += message.noteNumber
              sustainedNotes -= message.noteNumber

            case message: NoteOff =>
              if (sustainActive) sustainedNotes += message.noteNumber
              else newActiveNotes -= message.noteNumber

            // Pedal pressed - todo: move controller number to config
            case ControlChange(64, 127) =>
              sustainActive = true

            // Pedal released
            case ControlChange(64, 0) =>
              sustainActive = false
              newActiveNotes = newActiveNotes -- sustainedNotes
              sustainedNotes = Set()

            case ControlChange(controller, value) => log.info(s"Controller $controller changed to $value")
          }

          if (newActiveNotes == activeNotes) {
            pull(in)
          } else {
            activeNotes = newActiveNotes
            push(out, Interpretation.allOf(activeNotes.toList))
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
