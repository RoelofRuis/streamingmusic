package util

package object timed {

  /** Millis since epoch */
  type Moment = Long

  case class TimeWindow[A](start: Moment, end: Moment, contents: A) {
    val dur: Long = end - start
  }

  case class TimeGrid[A](windows: List[TimeWindow[A]]) {
    def map[B](f: A => B): TimeGrid[B] = {
      TimeGrid(windows.map { w => TimeWindow(w.start, w.end, f(w.contents))})
    }

    def describe(): Unit = {
      def loop(l: List[(TimeWindow[_], Int)]): Unit = {
        if (l.nonEmpty) {
          val head = l.head._1
          val index = l.head._2
          val eventDescription = head.contents
          println(f"$index. (${head.start} to ${head.end}) took ${head.dur}:\n$eventDescription\n")
          loop(l.tail)
        }
      }

      if (windows.isEmpty) println("Empty Grid")
      else loop(windows.zipWithIndex)
    }
  }
}
