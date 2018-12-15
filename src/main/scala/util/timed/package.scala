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
  }
}
