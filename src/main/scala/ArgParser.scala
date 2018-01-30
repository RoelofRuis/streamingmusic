object ArgParser {
  def parseOptions(map: Map[String, Any], list: List[String]): Map[String, Any] = {
    list match {
      case Nil => map
      case "-s" :: value :: tail => parseOptions(map ++ Map("storageFile" -> value), tail)
      case "--raw" :: value :: tail => parseOptions(map ++ Map("rawFile" -> value), tail)
      case option :: tail => println("Unknown option " + option)
        sys.exit(0)
    }
  }
}
