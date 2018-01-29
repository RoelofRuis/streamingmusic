package stream

import akka.stream._
import akka.stream.stage._
import midi.{TimedMessage, MidiByte, Parser}

class MessageParser extends GraphStage[FlowShape[Byte, TimedMessage]] {

  val in: Inlet[Byte] = Inlet[Byte]("graph.MusicEventParser.in")
  val out: Outlet[TimedMessage] = Outlet[TimedMessage]("graph.MusicEventParser.out")

  val shape: FlowShape[Byte, TimedMessage] = FlowShape.of(in, out)

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
