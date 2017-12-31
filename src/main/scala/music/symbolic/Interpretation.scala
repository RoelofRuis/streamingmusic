package music.symbolic

case class Interpretation(root: MVec, intervals: Set[MVec]) {
  def mapIntervals[A](f: MVec => A): Set[A] = intervals.map(f)
  def mapNotes[A](f: MVec => A): Set[A] = intervals.map(_ + root).map(f)
}
