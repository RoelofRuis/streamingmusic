package data.timed

import data.timed.TimeGrid.TimeWindow

case class TimeGrid[A](windows: List[TimeWindow[A]]) {
  def map[B](f: A => B): TimeGrid[B] = {
    TimeGrid(windows.map { w => TimeWindow(w.start, w.end, f(w.contents))})
  }
}

object TimeGrid {

  case class TimeWindow[A](start: Moment, end: Moment, contents: A) {
    val dur: Long = end - start

    def +(other: TimeWindow[A])(implicit combineContents: (A, A) => A): TimeWindow[A] = {
      TimeWindow(
        start,
        other.end,
        combineContents(contents, other.contents)
      )
    }
  }

}