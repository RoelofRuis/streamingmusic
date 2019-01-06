package util.properties

import scala.reflect.ClassTag
import scala.language.existentials

case class PropertyList[A](subject: A, props: List[P forSome { type P <: Property }]) {
  def has[T <: Property : ClassTag]: Boolean = props.exists {
    case _: T => true
    case _ => false
  }

  def withProperties(extractors: List[Property.Extractor[A, P forSome { type P <: Property}]]): PropertyList[A] = {
    PropertyList(subject, extractors.map(extractor => extractor(subject)))
  }
}

object PropertyList {
  def wrap[A](subject: A): PropertyList[A] = PropertyList(subject, List())
}