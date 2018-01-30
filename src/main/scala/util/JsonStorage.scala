package util

import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.write

object JsonStorage {
  private implicit val formats: DefaultFormats.type = DefaultFormats

  def storeObjectInFile[A <: AnyRef](obj: A, fname: String): Unit = {
    import java.io._

    val writer = new PrintWriter(new File(s"raw/$fname.json"))
    val objectData = write[A](obj)
    writer.write(objectData)
    writer.close()
  }

}
