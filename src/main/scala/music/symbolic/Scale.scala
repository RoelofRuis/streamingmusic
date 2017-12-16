package music.symbolic

case class Scale(s: MVec*) {
  def tonic: MVec = s.head

  def transpose(v: MVec)(implicit ns: NotationSystem): Scale = Scale(s.map(_ + v): _*)

  def numNotes: Int = s.length
}
