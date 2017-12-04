import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import akka.util.{ByteIterator, ByteString}

class BytestringSplitter extends GraphStage[FlowShape[ByteString, Byte]] {
  val in: Inlet[ByteString] = Inlet[ByteString]("BytestringSplitter.in")
  val out: Outlet[Byte] = Outlet[Byte]("BytestringSplitter.out")

  val shape: FlowShape[ByteString, Byte] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {
      var currentIterator: Option[ByteIterator] = None

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val byteIterator = grab(in).iterator
          currentIterator = Some(byteIterator)
          push(out, currentIterator.get.next)
        }
      })

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          if (currentIterator.isDefined && currentIterator.get.hasNext) {
            push(out, currentIterator.get.next)
          } else {
            pull(in)
          }
        }
      })
    }
}
