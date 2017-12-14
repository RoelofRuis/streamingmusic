package music.symbolic

object Naming {
  def standard(v: MVec): String = {
    val baseName = v.n match {
      case 0 => "C"
      case 1 => "D"
      case 2 => "E"
      case 3 => "F"
      case 4 => "G"
      case 5 => "A"
      case 6 => "B"
      case _ => "?"
    }
    val accidentalName = v.a match {
      case 0 => ""
      case i if i > 0 => "#" * i
      case i if i < 0 => "b" * -i
    }
    baseName + accidentalName
  }

  def pitch(v: MVec): String = {
    (v.n + v.a).toString
  }
}
