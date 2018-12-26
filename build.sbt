name := "scala-akka-graph-examples"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.0",
  
  "com.typesafe.akka" %% "akka-stream" % "2.5.19",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.19" % Test,
  
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  
  "com.typesafe.akka" %% "akka-actor" % "2.5.19",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.19" % Test,

  "com.typesafe.akka" %% "akka-http" % "10.1.6",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.6" % Test,

  "org.scalactic" %% "scalactic" % "3.0.5",

  "com.lightbend.akka" %% "akka-stream-alpakka-google-cloud-pub-sub" % "1.0-M1"
)
