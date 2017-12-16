package graph

import akka.stream.stage.StageLogging
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import music.MidiNote
import music.symbolic.{NotationSystem, NoteName}

class NoteInterpreter extends GraphStage[FlowShape[Set[MidiNote], Set[String]]] {
  val in: Inlet[Set[MidiNote]] = Inlet[Set[MidiNote]]("graph.NoteInterpreter.in")
  val out: Outlet[Set[String]] = Outlet[Set[String]]("graph.NoteInterpreter.out")

  val shape: FlowShape[Set[MidiNote], Set[String]] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) with StageLogging {
      implicit val ns: NotationSystem = NotationSystem(Seq(2, 2, 1, 2, 2, 2, 1))

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val midiNotes = grab[Set[MidiNote]](in)
          val noteList = midiNotes.map(_.noteNumber % ns.numSteps).map(ns.notes).map(NoteName(_))

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
