package util.properties

trait Property

object Property {
  type Extractor[A, P <: Property] = A => P
}
