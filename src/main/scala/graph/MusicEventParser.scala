package graph

import akka.stream._
import akka.stream.stage._
import music._
import util.MidiByte

import scala.collection.mutable.ListBuffer

class MusicEventParser extends GraphStage[FlowShape[Byte, MusicEvent]] {

  val in: Inlet[Byte] = Inlet[Byte]("graph.MusicEventParser.in")
  val out: Outlet[MusicEvent] = Outlet[MusicEvent]("graph.MusicEventParser.out")

  val shape: FlowShape[Byte, MusicEvent] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape: Shape) with StageLogging {
      var status: Option[MidiByte] = None
      val data: ListBuffer[MidiByte] = ListBuffer()

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val current = MidiByte(grab(in))

          if (current.isStatusByte) {
            if (!current.isSystemRealtime) {
              status = None
              data.clear()
            }

            if (current.isNoteOn) {
              status = Some(current)
            }
          } else {
            if (status.isDefined && status.get.isNoteOn) {
              data.append(current)
              if (data.length == 2) {
                val velocityByte: Byte = data.apply(1).byte
                if (velocityByte == 0) {
                  push(out, NoteOff(data.head.byte))
                } else {
                  push(out, NoteOn(data.head.byte, velocityByte))
                }
                return
              }
            }
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
