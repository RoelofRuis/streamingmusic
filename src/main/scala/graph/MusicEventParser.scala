package graph

import akka.stream._
import akka.stream.stage._
import midi.{Message, MidiByte, Parser}

class MusicEventParser extends GraphStage[FlowShape[Byte, Message]] {

  val in: Inlet[Byte] = Inlet[Byte]("graph.MusicEventParser.in")
  val out: Outlet[Message] = Outlet[Message]("graph.MusicEventParser.out")

  val shape: FlowShape[Byte, Message] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape: Shape) with StageLogging {
      var parser = Parser()

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val (nextParser, message) = parser.nextState(MidiByte(grab(in)))
          parser = nextParser

          if (message.isDefined) {
            push(out, message.get)
            return
          }

          pull(in)
        }
      })

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          pull(in)
        }
      })
    }
}
