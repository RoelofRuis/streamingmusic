package music.symbolic

case class Interpretation(root: MVec, intervals: Set[MVec]) {
  def mapIntervals[A](f: MVec => A): Set[A] = intervals.map(f)
}
