name := "test-app"

version := "1.0"

scalaVersion := "2.11.7"

val akka = Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.5",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.4"
)

val logging = Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "org.slf4j" % "slf4j-api" % "1.7.21"
)

val tests = Seq(
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

val other = Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6"
)

libraryDependencies ++= akka ++ logging ++ tests ++ other