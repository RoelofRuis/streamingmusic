package graph

import akka.stream.stage._
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import music.symbolic.{MVec, NoteName}
import types.{NoteNumber, Simultaneous}

class NoteInterpreter extends GraphStage[FlowShape[Simultaneous[NoteNumber], Simultaneous[Set[String]]]] {
  val in: Inlet[Simultaneous[NoteNumber]] = Inlet[Simultaneous[NoteNumber]]("graph.NoteInterpreter.in")
  val out: Outlet[Simultaneous[Set[String]]] = Outlet[Simultaneous[Set[String]]]("graph.NoteInterpreter.out")

  val shape: FlowShape[Simultaneous[NoteNumber], Simultaneous[Set[String]]] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with StageLogging {
      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val midiNotes = grab[Simultaneous[NoteNumber]](in)
          val noteList = midiNotes.map(_ % 12).map(MVec(0).interpret(_).mapIntervals[String](NoteName(_)))

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
