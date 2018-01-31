package util

import midi.{ControlChange, NoteOff, NoteOn}
import org.json4s.{DefaultFormats, ShortTypeHints, TypeHints}
import org.json4s.jackson.Serialization.write

/**
  * TODO: Remove hard ties with 'midi' package and specific message types.
  */
object JsonStorage {
  private implicit val formats: DefaultFormats = new DefaultFormats {
    override val typeHintFieldName = "_type"
    override val typeHints: TypeHints = ShortTypeHints(List(classOf[NoteOn], classOf[NoteOff], classOf[ControlChange]))
  }

  def storeObjectInFile[A <: AnyRef](obj: Seq[A], fname: String): Unit = {
    import java.io._

    val writer = new PrintWriter(new File(s"data/$fname.json"))
    for (a <- obj) {
      val data = write[A](a) + "\n"
      writer.append(data)
    }
    writer.close()
  }

}
