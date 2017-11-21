name := "serial-midi"

version := "0.1"

scalaVersion := "2.11.6"

lazy val akkaVersion = "2.5.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion,
)

libraryDependencies ++= Seq(
  "ch.jodersky" %% "akka-serial-core" % "4.1.1",
  "ch.jodersky" % "akka-serial-native" % "4.1.1" % "runtime",
)