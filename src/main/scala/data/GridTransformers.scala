package data

import data.timed.TimeGrid
import data.timed.TimeGrid.TimeWindow

object GridTransformers {
  def flatten[A](windows: List[TimeWindow[List[A]]]): TimeWindow[List[A]] =
    TimeWindow(
      windows.head.start,
      windows.last.end,
      windows.flatMap(_.contents).distinct
    )

  def groupActivity[A](g: TimeGrid[List[A]]): TimeGrid[List[A]] = {
    def resolve(windows: List[TimeWindow[List[A]]]): TimeGrid[List[A]] = {
      var curWindows = List(windows.head)
      var resultWindows = List[TimeWindow[List[A]]]()
      var isLastEmpty = windows.head.contents.isEmpty

      for (next <- windows.tail) {
        val isEmpty = next.contents.isEmpty
        if (isLastEmpty) {
          if (isEmpty) curWindows :+= next
          else {
            resultWindows :+= flatten(curWindows)
            curWindows = List(next)
          }
        } else {
          if (isEmpty) {
            resultWindows :+= flatten(curWindows)
            curWindows = List(next)
          }
          else curWindows :+= next
        }
        isLastEmpty = isEmpty
      }

      resultWindows :+= flatten(curWindows)

      TimeGrid(resultWindows)
    }

    if (g.windows.size < 2) g
    else resolve(g.windows)
  }


}
