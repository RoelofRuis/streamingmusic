name := "serial-midi"

version := "0.1"

scalaVersion := "2.12.2"

val akkaVersion = "2.5.9"
val akkaSerialVersion = "4.1.1"
val typesafeConfigVersion = "1.3.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
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

libraryDependencies += "org.specs2" %% "specs2-core" % "4.0.2" % Test

libraryDependencies += "com.typesafe" % "config" % typesafeConfigVersion
