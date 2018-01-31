object ArgParser {
  def parseOptions(map: Map[String, Any], list: List[String]): Map[String, Any] = {
    list match {
      case Nil => map
      case "-s" :: tail => parseOptions(map ++ Map("store" -> true), tail)
      case "-l" :: value :: tail => parseOptions(map ++ Map("dataFile" -> value), tail)
      case option :: tail => println("Unknown option " + option)
        sys.exit(0)
    }
  }
}
