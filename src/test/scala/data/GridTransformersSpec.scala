package data

import data.timed.TimeGrid
import data.timed.TimeGrid.TimeWindow
import org.scalatest.WordSpec

class GridTransformersSpec extends WordSpec {

  "A `group` transformation" when {
    "given an empty grid" should {
      "return an empty grid" in {
        val in = grid()
        assert(GridTransformers.group(in) == in)
      }
    }

    "given a grid with one window" should {
      "return it unchanged" in {
        val in = grid(window(0, 1000))
        assert(GridTransformers.group(in) == in)
      }
    }

    "given a grid with two windows with the same event" should {
      "concatenate them into one" in {
        val in = grid(
          window(0, 1000, MusicEvent(1)),
          window(1000, 2000, MusicEvent(1))
        )
        val out = grid(
          window(0, 2000, MusicEvent(1))
        )
        assert(GridTransformers.group(in) == out)
      }
    }

    "given a grid with two windows with a different event" should {
      "return them unchanged" in {
        val in = grid(
          window(0, 1000, MusicEvent(1)),
          window(1000, 2000, MusicEvent(2))
        )
        assert(GridTransformers.group(in) == in)
      }
    }

    "given a grid with two windows with the second adding an event" should {
      "return the concatenated window with both events" in {
        val in = grid(
          window(0, 10, MusicEvent(1)),
          window(10, 1000, MusicEvent(1), MusicEvent(2))
        )
        val out = grid(
          window(0, 1000, MusicEvent(1), MusicEvent(2))
        )
        assert(GridTransformers.group(in) == out)
      }
    }
  }

  def window(start: Int, end: Int, evts: MusicEvent*): TimeWindow[List[MusicEvent]] = {
    TimeWindow(start, end, evts.toList)
  }

  def grid(windows: TimeWindow[List[MusicEvent]]*): TimeGrid[List[MusicEvent]] = {
    TimeGrid(windows.toList)
  }

}
