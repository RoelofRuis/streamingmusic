package util.timed

trait TimeGridBuilder[K, A, B] {
  def start(moment: Moment, key: K, elem: A): Unit
  def end(moment: Moment, key: K): Unit
  def cut(moment: Moment): Unit
  def grid(): TimeGrid[B]
}

/**
  * Produces a time grid consisting of windows of simultaneously occurring events.
  *
  * @tparam A The type of element to be stored
  * @tparam K The key type
  */
class SimultaneousTimeGridBuilder[A, K] extends TimeGridBuilder[K, A, List[A]] {
  private var lastMoment: Option[Moment] = None
  private var activeKeys: Map[K, A] = Map[K, A]()
  private var windows: List[TimeWindow[List[A]]] = List[TimeWindow[List[A]]]()

  def start(moment: Moment, key: K, elem: A): Unit = {
    cut(moment)
    activeKeys += (key -> elem)
  }

  def end(moment: Moment, key: K): Unit = {
    cut(moment)
    activeKeys -= key
  }

  def cut(moment: Moment): Unit = {
    if (lastMoment.isDefined) {
      windows :+= TimeWindow(lastMoment.get, moment, activeKeys.values.toList)
    }

    lastMoment = Some(moment)
  }

  def grid(): TimeGrid[List[A]] = TimeGrid(windows)
}
