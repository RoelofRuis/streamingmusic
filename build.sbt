name := "serial-midi"

version := "0.1"

scalaVersion := "2.12.2"

lazy val akkaVersion = "2.5.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
)

val akkaSerialVersion = "4.1.1"

libraryDependencies ++= Seq(
  "ch.jodersky" %% "akka-serial-core" % akkaSerialVersion,
  "ch.jodersky" % "akka-serial-native" % akkaSerialVersion % "runtime",
  "ch.jodersky" %% "akka-serial-stream" % akkaSerialVersion
)

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"