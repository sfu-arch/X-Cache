// name := "dandelion-lib"

import sbt.complete._
import sbt.complete.DefaultParsers._
import xerial.sbt.pack._
import sys.process._

enablePlugins(PackPlugin)

lazy val commonSettings = Seq(
  name := "dandelion-lib",
  organization := "edu.sfu.cs",
  version      := "1.0-SNAPSHOT",
  scalaVersion := "2.12.10",
  parallelExecution in Global := true,
  parallelExecution in Test := true,
  logBuffered in Test := false,
  testOptions in Test += Tests.Argument("-oDS"),
  traceLevel   := 15,
  scalacOptions ++= Seq("-deprecation","-unchecked","-Xsource:2.11"),
  libraryDependencies ++= Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value),
  libraryDependencies ++= Seq("org.json4s" %% "json4s-jackson" % "3.6.1"),
  libraryDependencies ++= Seq("com.lihaoyi" %% "sourcecode" % "0.1.4"),
  libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "3.0.1"),
  libraryDependencies ++= Seq("org.scalacheck" %% "scalacheck" % "1.13.4"),
  libraryDependencies ++= Seq("edu.berkeley.cs" %% "chisel-iotesters" % "1.3-SNAPSHOT"),
  libraryDependencies ++= Seq("edu.berkeley.cs" %% "dsptools" % "1.2-SNAPSHOT"),
  libraryDependencies ++= Seq("edu.berkeley.cs" %% "chiseltest" % "0.2.1"),


    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  resolvers ++= Seq(
    Resolver.sonatypeRepo("snapshots"),
    Resolver.sonatypeRepo("releases"),
    Resolver.mavenLocal
  )
)

// lazy val chisel = (project in file("chisel3")).settings(commonSettings)

def dependOnChisel(prj: Project) = {
  prj.settings(
    libraryDependencies ++= Seq("edu.berkeley.cs" %% "chisel3" % "3.3-SNAPSHOT")
    )
}

def dependOnIoTesters(prj: Project) = {
  prj.settings(
    libraryDependencies ++= Seq("edu.berkeley.cs" %% "chisel-iotesters" % "1.3-SNAPSHOT"),
      libraryDependencies ++= Seq("edu.berkeley.cs" %% "chiseltest" % "0.2.1")
    )


}


lazy val `api-config-chipsalliance` = (project in file("api-config-chipsalliance/build-rules/sbt"))
  .settings(commonSettings)
  .settings(publishArtifact := false)
lazy val hardfloat  = dependOnChisel(project)
  .settings(commonSettings)
  .settings(publishArtifact := false)

lazy val dandelion= dependOnChisel(project in file("."))
  .settings(commonSettings, chipSettings)
  .dependsOn(`api-config-chipsalliance` % "compile-internal;test-internal")
  .dependsOn(hardfloat % "compile-internal;test-internal")
  .settings(
      aggregate := false,
      // Include macro classes, resources, and sources in main jar.
      mappings in (Compile, packageBin) ++= (mappings in (`api-config-chipsalliance`, Compile, packageBin)).value,
      mappings in (Compile, packageSrc) ++= (mappings in (`api-config-chipsalliance`, Compile, packageSrc)).value,
      mappings in (Compile, packageBin) ++= (mappings in (hardfloat, Compile, packageBin)).value,
      mappings in (Compile, packageSrc) ++= (mappings in (hardfloat, Compile, packageSrc)).value,
      exportJars := true
  )

lazy val addons = settingKey[Seq[String]]("list of addons used for this build")
lazy val make = inputKey[Unit]("trigger backend-specific makefile command")
val setMake = NotSpace ~ ( Space ~> NotSpace )

lazy val chipSettings = Seq(
  addons := {
    val a = sys.env.getOrElse("DANDELION_ADDONS", "")
    println(s"Using addons: $a")
    a.split(" ")
  },
  unmanagedSourceDirectories in Compile ++= addons.value.map(baseDirectory.value / _ / "src/main/scala"),
  mainClass in (Compile, run) := Some("dandelion.Generator"),
  make := {
    val jobs = java.lang.Runtime.getRuntime.availableProcessors
    val (makeDir, target) = setMake.parsed
    (run in Compile).evaluated
    s"make -C $makeDir  -j $jobs $target".!
  }
)

