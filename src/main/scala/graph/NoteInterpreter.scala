package graph

import akka.stream.stage._
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import music.symbolic.{MVec, NotationSystem, NoteName, StandardNotation}
import types.{NoteNumber, OneOf, Simultaneous}

class NoteInterpreter extends GraphStage[FlowShape[Simultaneous[NoteNumber], Simultaneous[OneOf[String]]]] {
  val in: Inlet[Simultaneous[NoteNumber]] = Inlet[Simultaneous[NoteNumber]]("graph.NoteInterpreter.in")
  val out: Outlet[Simultaneous[OneOf[String]]] = Outlet[Simultaneous[OneOf[String]]]("graph.NoteInterpreter.out")

  val shape: FlowShape[Simultaneous[NoteNumber], Simultaneous[OneOf[String]]] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with StageLogging {
      implicit val ns: NotationSystem = StandardNotation

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val midiNotes = grab[Set[NoteNumber]](in)
          val noteList = midiNotes.map(ns.midi2pc).map(MVec(0).interpret(_).map(NoteName(_)))

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
