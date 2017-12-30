package graph

import akka.stream.stage._
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import midi.Midi
import music.symbolic.{MVec, NotationSystem, NoteName, StandardNotation}

class NoteInterpreter extends GraphStage[FlowShape[Set[Midi#NoteNumber], Set[String]]] {
  val in: Inlet[Set[Midi#NoteNumber]] = Inlet[Set[Midi#NoteNumber]]("graph.NoteInterpreter.in")
  val out: Outlet[Set[String]] = Outlet[Set[String]]("graph.NoteInterpreter.out")

  val shape: FlowShape[Set[Midi#NoteNumber], Set[String]] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with StageLogging {
      implicit val ns: NotationSystem = StandardNotation

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val midiNotes = grab[Set[Midi#NoteNumber]](in)
          val noteList = midiNotes.map(ns.midi2pc).flatMap(MVec(0, 0).interpret(_).map(NoteName(_)))

          push(out, noteList)
        }
      })

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          pull(in)
        }
      })
    }
}
