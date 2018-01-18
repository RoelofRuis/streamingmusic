package music.knowledge

import types.{Interval, IntervalFunction}
import music.symbolic.MVec

object Intervals {
  def asFunction(i: Interval): List[IntervalFunction] = {
    i match {
      case MVec(0, 0) => root :: Nil
      case MVec(1, -1) => flatNine :: Nil
      case MVec(1, 0) => two :: nine :: Nil
      case MVec(2, -1) => flatThree :: flatTen :: Nil
      case MVec(2, 0) => three :: Nil
      case MVec(3, 0) => four :: eleven :: Nil
      case MVec(3, 1) => sharpEleven :: flatFive :: Nil
      case MVec(4, 0) => five :: Nil
      case MVec(4, 1) => sharpFive :: Nil
      case MVec(5, -1) => flatThirteen :: Nil
      case MVec(5, 0) => six :: thirteen :: Nil
      case MVec(6, -2) => diminishedSeven :: Nil
      case MVec(6, -1) => flatSeven :: Nil
      case MVec(6, 0) => seven :: Nil
      case _ => Nil
    }
  }

  val root = MVec(0, 0)
  val two = MVec(1, 0)
  val flatThree = MVec(2, -1)
  val three = MVec(2, 0)
  val four = MVec(3, 0)
  val flatFive = MVec(4, -1)
  val five = MVec(4, 0)
  val sharpFive = MVec(4, 1)
  val six = MVec(5, 0)
  val diminishedSeven = MVec(6, -2)
  val flatSeven = MVec(6, -1)
  val seven = MVec(6, 0)
  val flatNine = MVec(8, -1)
  val nine = MVec(8, 0)
  val flatTen = MVec(9, -1)
  val eleven = MVec(10, 0)
  val sharpEleven = MVec(10, 1)
  val flatThirteen = MVec(12, -1)
  val thirteen = MVec(12, 0)
}
