import akka.stream.{Inlet, Outlet, Shape}
import akka.stream.stage.{GraphStageLogic, InHandler, OutHandler, StageLogging}

import scala.collection.mutable.ListBuffer

class MidiParser(shape: Shape, in: Inlet[Byte], out: Outlet[MusicEvent]) extends GraphStageLogic(shape: Shape) with StageLogging {
  var status: Option[MidiByte] = None
  val data: ListBuffer[MidiByte] = ListBuffer()

  setHandler(in, new InHandler {
    override def onPush(): Unit = {
      val current = MidiByte(grab(in))

      if (current.isStatusByte) {
        if ( ! current.isSystemRealtime) {
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
