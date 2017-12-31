object ArgParser {
  def parseOptions(map: Map[String, Any], list: List[String]): Map[String, Any] = {
    list match {
      case Nil => map
      case "-f" :: value :: tail => parseOptions(map ++ Map("file" -> value), tail)
      case option :: tail => println("Unknown option " + option)
        sys.exit(0)
    }
  }
}
