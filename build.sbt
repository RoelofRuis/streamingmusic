name := "serial-midi"

version := "0.1"

scalaVersion := "2.12.2"

val akkaVersion = "2.5.6"
val akkaSerialVersion = "4.1.1"
val json4sVersion = "3.6.0-M2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.1.0-RC1",
)


libraryDependencies ++= Seq(
  "ch.jodersky" %% "akka-serial-core" % akkaSerialVersion,
  "ch.jodersky" % "akka-serial-native" % akkaSerialVersion % "runtime",
  "ch.jodersky" %% "akka-serial-stream" % akkaSerialVersion
)

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

libraryDependencies += "org.json4s" %% "json4s-native" % json4sVersion
