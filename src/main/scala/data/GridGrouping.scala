package data

import data.timed.TimeGrid
import data.timed.TimeGrid.TimeWindow

// TODO: factor out commonalities
object GridGrouping {
  implicit def +[A](a: List[A], b: List[A]): List[A] = (a ++ b).distinct

  def nonEmpty[A](g: TimeGrid[List[A]]): TimeGrid[List[A]] = {
    def resolve(windows: List[TimeWindow[List[A]]]): TimeGrid[List[A]] = {
      var curWindows = if (windows.head.contents.isEmpty) List() else List(windows.head)
      var resultWindows = List[TimeWindow[List[A]]]()

      for (next <- windows.tail) {
        val isEmpty = next.contents.isEmpty
        if (! isEmpty) {
          curWindows :+= next
        }
        else {
          resultWindows :+= curWindows.reduce(_ + _)
          curWindows = List()
        }
      }

      resultWindows :+= curWindows.reduce(_ + _)

      TimeGrid(resultWindows)
    }

    if (g.windows.size < 2) g
    else resolve(g.windows)
  }

  def activity[A](g: TimeGrid[List[A]]): TimeGrid[List[A]] = {
    def resolve(windows: List[TimeWindow[List[A]]]): TimeGrid[List[A]] = {
      var curWindows = List(windows.head)
      var resultWindows = List[TimeWindow[List[A]]]()
      var isLastEmpty = windows.head.contents.isEmpty

      for (next <- windows.tail) {
        val isEmpty = next.contents.isEmpty
        if (isLastEmpty) {
          if (isEmpty) curWindows :+= next
          else {
            resultWindows :+= curWindows.reduce(_ + _)
            curWindows = List(next)
          }
        } else {
          if (isEmpty) {
            resultWindows :+= curWindows.reduce(_ + _)
            curWindows = List(next)
          }
          else curWindows :+= next
        }
        isLastEmpty = isEmpty
      }

      resultWindows :+= curWindows.reduce(_ + _)

      TimeGrid(resultWindows)
    }

    if (g.windows.size < 2) g
    else resolve(g.windows)
  }


}
