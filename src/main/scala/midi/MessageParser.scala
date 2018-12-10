package midi

import akka.stream._
import akka.stream.stage._

class MessageParser extends GraphStage[FlowShape[Byte, TimedMessage]] {

  val in: Inlet[Byte] = Inlet[Byte]("graph.MessageParser.in")
  val out: Outlet[TimedMessage] = Outlet[TimedMessage]("graph.MessageParser.out")

  val shape: FlowShape[Byte, TimedMessage] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape: Shape) with StageLogging with InHandler with OutHandler {

      private var parser = Parser()

      override def onPush(): Unit = {
        val (nextParser, message) = parser.nextState(MidiByte(grab(in)))
        parser = nextParser

        if (message.isDefined) push(out, TimedMessage(System.currentTimeMillis(), message.get))
        else pull(in)
      }

      override def onPull(): Unit = {
        pull(in)
      }

      setHandlers(in, out, this)
    }
}
