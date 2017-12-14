package music.symbolic

object Interval {
  def perfectPrime: MVec = MVec(0)
  def augmentedPrime: MVec = MVec(0, 1)

  def diminishedSecond: MVec = MVec(1, -2)
  def minorSecond: MVec = MVec(1, -1)
  def majorSecond: MVec = MVec(1)
  def augmentedSecond: MVec = MVec(1, 1)

  def diminishedThird: MVec = MVec(2, -2)
  def minorThird: MVec = MVec(2, -1)
  def majorThird: MVec = MVec(2)
  def augmentedThird: MVec = MVec(2, 1)

  def diminishedFourth: MVec = MVec(3, -1)
  def perfectFourth: MVec = MVec(3)
  def augmentedFourth: MVec = MVec(3, 1)

  def diminishedFifth: MVec = MVec(4, -1)
  def perfectFifth: MVec = MVec(4)
  def augmentedFifth: MVec = MVec(4, 1)

  def diminishedSixth: MVec = MVec(5, -2)
  def minorSixth: MVec = MVec(5, -1)
  def majorSixth: MVec = MVec(5)
  def augmentedSixth: MVec = MVec(5, 1)

  def diminishedSeventh: MVec = MVec(6, -2)
  def minorSeventh: MVec = MVec(6, -1)
  def majorSeventh: MVec = MVec(6)
  def augmentedSeventh: MVec = MVec(6, 1)
}