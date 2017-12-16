package graph

import akka.stream.stage._
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import music.{MidiNote, MusicEvent, NoteOff, NoteOn}

class ChordAggregator extends GraphStage[FlowShape[MusicEvent, Set[MidiNote]]] {
  val in: Inlet[MusicEvent] = Inlet[MusicEvent]("graph.ChordAggregator.in")
  val out: Outlet[Set[MidiNote]] = Outlet[Set[MidiNote]]("graph.ChordAggregator.out")

  val shape: FlowShape[MusicEvent, Set[MidiNote]] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with StageLogging {
      var activeNotes: Set[MidiNote] = Set()

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          grab[MusicEvent](in) match {
            case event: NoteOn => activeNotes += event.note
            case event: NoteOff => activeNotes -= event.note
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
