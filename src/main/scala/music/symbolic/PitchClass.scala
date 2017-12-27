package music.symbolic

case class PitchClass(n: Int) {
  def +(other: Int): PitchClass = PitchClass(n + other)

  def -(other: PitchClass): PitchClass = PitchClass(n - other.n)

  def +(other: PitchClass): PitchClass = PitchClass(n + other.n)
}
