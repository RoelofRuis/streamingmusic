package data

import data.timed.TimeGrid
import data.timed.TimeGrid.TimeWindow

object GridTransformers {
  def group(grid: TimeGrid[List[MusicEvent]]): TimeGrid[List[MusicEvent]] = {
    def resolveWindows(windows: List[TimeWindow[List[MusicEvent]]]): TimeGrid[List[MusicEvent]] = {
      var curContents = windows.head.contents
      var curStart = windows.head.start
      var curEnd = windows.head.end
      var resultWindows = List[TimeWindow[List[MusicEvent]]]()

      for(next <- windows.tail) {
        val nextContents = next.contents
        if (curContents.map(_.midiNoteNumber).forall(nextContents.map(_.midiNoteNumber).contains)) {
          curContents = nextContents
          curEnd = next.end
        } else {
          resultWindows :+= TimeWindow(curStart, curEnd, curContents)
          curContents = nextContents
          curStart = next.start
          curEnd = next.end
        }
      }
      resultWindows :+= TimeWindow(curStart, curEnd, curContents)

      TimeGrid(resultWindows)
    }

    grid.windows match {
      case _ :: _ :: _ => resolveWindows(grid.windows)
      case _ => grid
    }
  }
}
