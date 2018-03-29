name := "dataflowLib"

organization := "sfu.arch"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.11"

scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked", "-language:reflectiveCalls")

/**
  D - show durations
  S - show short stack traces
  F - show full stack traces
  W - Without color 
  **/
testOptions in Test += Tests.Argument("-oD")

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

// Provide a managed dependency on X if -DXVersion="" is supplied on the command line.
val defaultVersions = Map(
//  "chisel3" -> "3.0-SNAPSHOT",
  "chisel3" -> "3.0.+",
  "chisel-iotesters" -> "1.1.+"
  )

libraryDependencies ++= (Seq("chisel3","chisel-iotesters").map {
  dep: String => "edu.berkeley.cs" %% dep % sys.props.getOrElse(dep + "Version", defaultVersions(dep)) })

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1",
  "org.scalacheck" %% "scalacheck" % "1.13.4",
  "com.lihaoyi" %% "sourcecode" % "0.1.4" // Scala-JVM
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases")
)

// Recommendations from http://www.scalatest.org/user_guide/using_scalatest_with_sbt
logBuffered in Test := false

// Disable parallel execution when running te
//  Running tests in parallel on Jenkins currently fails.
parallelExecution in Test := false

