package midi

import org.json4s.native.JsonMethods._
import org.json4s.{DefaultFormats, Extraction, ShortTypeHints, TypeHints}

object JsonTimedMessages {
  private implicit val formats: DefaultFormats = new DefaultFormats {
    override val typeHintFieldName = "_type"
    override val typeHints: TypeHints = ShortTypeHints(List(classOf[NoteOn], classOf[NoteOff], classOf[ControlChange]))
  }

  def deserializeOne(json: String): TimedMessage = parse(json).extract[TimedMessage]

  def writeAllToFile(obj: Seq[TimedMessage], fname: String): Unit = {
    import java.io._

    val writer = new PrintWriter(new File(s"data/$fname.json"))
    for (a <- obj) {
      val data = compact(render(Extraction.decompose(a))) + "\n"
      writer.append(data)
    }
    writer.close()
  }

}
