package graph

import akka.stream.stage._
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import midi.{Message, Midi, NoteOff, NoteOn}

class ChordAggregator extends GraphStage[FlowShape[Message, Set[Midi#NoteNumber]]] {
  val in: Inlet[Message] = Inlet[Message]("graph.ChordAggregator.in")
  val out: Outlet[Set[Midi#NoteNumber]] = Outlet[Set[Midi#NoteNumber]]("graph.ChordAggregator.out")

  val shape: FlowShape[Message, Set[Midi#NoteNumber]] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with StageLogging {
      var activeNotes: Set[Midi#NoteNumber] = Set()

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          grab[Message](in) match {
            case message: NoteOn => activeNotes += message.noteNumber
            case message: NoteOff => activeNotes -= message.noteNumber
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
