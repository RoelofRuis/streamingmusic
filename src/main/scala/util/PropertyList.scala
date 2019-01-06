package util

import scala.language.existentials
import scala.reflect.ClassTag

/**
  * A data structure that can hold arbitrary properties and add additional properties about a subject
  *
  * @param subject The subject to which the properties apply
  * @param props   The current list of properties
  * @tparam A      The subject type
  * @tparam Props  The supertype of the properties
  */
case class PropertyList[A, Props] private (subject: A, props: List[T forSome { type T <: Props }]) {
  def get[P <: Props : ClassTag]: Option[P] = props.collectFirst { case prop: P => prop }

  def withProperties(extractors: List[A => P forSome { type P <: Props }]): PropertyList[A, Props] =
    PropertyList(subject, extractors.map(extractor => extractor(subject)))
}

object PropertyList {
  def wrap[A, Props](subject: A): PropertyList[A, Props] = PropertyList(subject, List())
}