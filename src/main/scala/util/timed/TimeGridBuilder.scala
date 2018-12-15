package util.timed

trait TimeGridBuilder[A, B] {
  def start(moment: Moment, elem: A): Unit
  def end(moment: Moment, elem: A): Unit
  def cut(moment: Moment): Unit
  def grid(): TimeGrid[B]
}

/**
  * Produces a time grid consisting of windows of simultaneously occurring events.
  *
  * @param start The initial moment this builder starts to take in events.
  * @param index Index function that describes how for each element A a key of type K can be produced.
  * @tparam A The type of element to be stored
  * @tparam K The key type
  */
class SimultaneousTimeGridBuilder[A, K](start: Moment, index: A => K) extends TimeGridBuilder[A, List[A]] {
  private var lastMoment: Moment = start
  private var activeKeys: Map[K, A] = Map[K, A]()
  private var windows: List[TimeWindow[List[A]]] = List[TimeWindow[List[A]]]()

  def start(moment: Moment, elem: A): Unit = {
    cut(moment)
    activeKeys += (index(elem) -> elem)
  }

  def end(moment: Moment, elem: A): Unit = {
    cut(moment)
    activeKeys -= index(elem)
  }

  def cut(moment: Moment): Unit = {
    windows :+= TimeWindow(lastMoment, moment, activeKeys.values.toList)
    lastMoment = moment
  }

  def grid(): TimeGrid[List[A]] = TimeGrid(windows)
}
