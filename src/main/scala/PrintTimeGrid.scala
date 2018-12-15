import midi.NoteOn
import util.timed.{TimeGrid, TimeWindow}

object PrintTimeGrid {

  implicit def prettyNotes(l: List[NoteOn]): String = {
    "Notes " + l.map(_.noteNumber).mkString("[", " ", "]")
  }

  def describe[A](grid: TimeGrid[A])(implicit contentDescr: A => String): Unit = {
    def loop(l: List[(TimeWindow[A], Int)], t0: Long): Unit = {
      if (l.nonEmpty) {
        val head = l.head._1
        val index = l.head._2
        val start = head.start - t0
        val end = head.end - t0
        println(f"Window $index [$start - $end] [dur ${head.dur}]\n${contentDescr(head.contents)}\n")
        loop(l.tail, t0)
      }
    }

    grid.windows.headOption match {
      case None       =>
        println("TimeGrid [Empty]")
      case Some(head) =>
        val t0 = head.start
        println(s"TimeGrid @ $t0")
        loop(grid.windows.zipWithIndex, t0)
    }
  }

}
