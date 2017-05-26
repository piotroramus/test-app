name := "test-app"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.5",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.4",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.6"
)
