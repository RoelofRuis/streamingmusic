package stream

import akka.stream._
import akka.stream.stage._
import midi.{ControlChange, MidiByte, Parser, TimedMessage}

class MessageParser extends GraphStage[FlowShape[Byte, TimedMessage]] {

  val in: Inlet[Byte] = Inlet[Byte]("graph.MusicEventParser.in")
  val out: Outlet[TimedMessage] = Outlet[TimedMessage]("graph.MusicEventParser.out")

  val shape: FlowShape[Byte, TimedMessage] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape: Shape) with StageLogging {

      private var measurementStart = System.currentTimeMillis()
      private var parser = Parser()

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val (nextParser, message) = parser.nextState(MidiByte(grab(in)))
          parser = nextParser

          if (message.isDefined) {
            val messageTime = System.currentTimeMillis() - measurementStart
            val parsedMessage = message.get
            push(out, TimedMessage(messageTime, parsedMessage))

            parsedMessage match {
              // TODO: Change hardcoded Left Pedal (Controller 67)
              case ControlChange(67, 0) => measurementStart = System.currentTimeMillis()
              case _ =>
            }

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
