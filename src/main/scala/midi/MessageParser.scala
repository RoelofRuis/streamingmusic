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
        val (nextParser, message) = parser.next(grab(in))
        parser = nextParser

        message match {
          case Some(msg) => push(out, TimedMessage(System.currentTimeMillis(), msg))
          case _         => pull(in)
        }
      }

      override def onPull(): Unit = pull(in)

      setHandlers(in, out, this)
    }
}
