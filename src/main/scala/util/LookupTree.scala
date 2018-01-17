package util

/**
  * Data structure that can be used for linear time lookup of B given a list of A.
  */
case class LookupTree[A, B](subNodes: Map[A, LookupTree[A, B]], nodeValue: Option[B]) {
  def store(l: List[A], value: B): LookupTree[A, B] = {
    if (l.isEmpty) LookupTree(subNodes, Some(value))
    else LookupTree(subNodes + (l.head -> subNodes.getOrElse(l.head, LookupTree.empty).store(l.tail, value)), nodeValue)
  }

  def find(l: List[A]): Option[B] = {
    if (l.isEmpty) nodeValue
    else subNodes.get(l.head).flatMap(_.find(l.tail))
  }
}

object LookupTree {
  def empty[A, B]: LookupTree[A, B] = LookupTree[A, B](Map(), None)

  def build[A, B](data: Map[B, List[A]]): LookupTree[A, B] = {
    data.foldLeft[LookupTree[A, B]](empty){case (tree, (value, list)) => tree.store(list, value)}
  }
}