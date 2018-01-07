package music.knowledge

sealed trait IntervalFunction extends Comparable[IntervalFunction] {
  val value: Int
  override def compareTo(other: IntervalFunction): Int = value.compareTo(other.value)
}

case object Root extends IntervalFunction {val value = 1}
case object Two extends IntervalFunction {val value = 2}
case object FlatThree extends IntervalFunction {val value = 3}
case object Three extends IntervalFunction {val value = 4}
case object Four extends IntervalFunction {val value = 5}
case object FlatFive extends IntervalFunction {val value = 6}
case object Five extends IntervalFunction {val value = 7}
case object SharpFive extends IntervalFunction {val value = 8}
case object Six extends IntervalFunction {val value = 9}
case object DiminishedSeven extends IntervalFunction {val value = 10}
case object FlatSeven extends IntervalFunction {val value = 11}
case object Seven extends IntervalFunction {val value = 12}
case object FlatNine extends IntervalFunction {val value = 13}
case object Nine extends IntervalFunction {val value = 14}
case object FlatTen extends IntervalFunction {val value = 15}
case object Eleven extends IntervalFunction {val value = 16}
case object SharpEleven extends IntervalFunction {val value = 17}
case object FlatThirteen extends IntervalFunction {val value = 18}
case object Thirteen extends IntervalFunction {val value = 19}

