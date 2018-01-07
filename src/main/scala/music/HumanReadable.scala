package music

import music.symbolic.MVec
import types._

// Defines all kinds of transformations from and to human-readable representation
trait HumanReadable[A] {
  def apply(obj: A): String

  def apply(s: String): A
}

object NoteName extends HumanReadable[MVec] {
  override def apply(note: Note): String = {
    val baseName = note.step match {
      case 0 => "C"
      case 1 => "D"
      case 2 => "E"
      case 3 => "F"
      case 4 => "G"
      case 5 => "A"
      case 6 => "B"
      case _ => "?"
    }
    val accidentalName = note.acc match {
      case 0 => ""
      case i if i > 0 => "#" * i
      case i if i < 0 => "b" * -i
    }
    baseName + accidentalName
  }

  override def apply(s: String): MVec = {
    throw new NotImplementedError()
  }
}

object Interval extends HumanReadable[MVec] {
  val intervals = Seq(
    ("prime", 0, true),
    ("second", 1, false),
    ("third", 2, false),
    ("fourth", 3, true),
    ("fifth", 4, true),
    ("sixth", 5, false),
    ("seventh", 6, false),
  )

  val adjustments = Seq(
    ("diminished", -2),
    ("minor", -1),
    ("major", 0),
    ("augmented", 1),
  )

  override def apply(i: Interval): String = {
    val intervalInfo = intervals.find(p => p._2 == i.step)
    val intervalName = intervalInfo.map(_._1).getOrElse(i.step)
    val usePerfect = intervalInfo.exists(_._3)
    var adjustmentName = adjustments.find(p => p._2 == i.acc).map(_._1).getOrElse(i.acc)

    if (usePerfect && adjustmentName == "major") {
      adjustmentName = "perfect"
    }
    adjustmentName + " " + intervalName
  }

  override def apply(s: String): MVec = {
    val parts = s.toLowerCase.replace("perfect", "major").split(" ")

    if (parts.length != 2) throw new Exception("Invalid interval representation " + s)

    val intervalInfo = intervals.find(p => p._1 == parts(1)).map(_._2)
    val adjustmentInfo = adjustments.find(p => p._1 == parts(0)).map(_._2)

    if (intervalInfo.isEmpty || adjustmentInfo.isEmpty) throw new Exception("Invalid interval representation " + s)

    MVec(intervalInfo.get, adjustmentInfo.get)
  }

  def perfectPrime: MVec = apply("perfect prime")

  def augmentedPrime: MVec = apply("augmented prime")

  def diminishedSecond: MVec = apply("diminished second")

  def minorSecond: MVec = apply("minor second")

  def majorSecond: MVec = apply("major second")

  def augmentedSecond: MVec = apply("augmented second")

  def diminishedThird: MVec = apply("diminished third")

  def minorThird: MVec = apply("minor third")

  def majorThird: MVec = apply("major third")

  def augmentedThird: MVec = apply("augmented third")

  def diminishedFourth: MVec = apply("diminished fourth")

  def perfectFourth: MVec = apply("perfect fourth")

  def augmentedFourth: MVec = apply("augmented fourth")

  def diminishedFifth: MVec = apply("diminished fifth")

  def perfectFifth: MVec = apply("perfect fifth")

  def augmentedFifth: MVec = apply("augmented fifth")

  def diminishedSixth: MVec = apply("diminished sixth")

  def minorSixth: MVec = apply("minor sixth")

  def majorSixth: MVec = apply("major sixth")

  def augmentedSixth: MVec = apply("augmented sixth")

  def diminishedSeventh: MVec = apply("diminished seventh")

  def minorSeventh: MVec = apply("minor seventh")

  def majorSeventh: MVec = apply("major seventh")

  def augmentedSeventh: MVec = apply("augmented seventh")
}