import sbt.complete._
import sbt.complete.DefaultParsers._
import xerial.sbt.pack._
import sys.process._

enablePlugins(PackPlugin)

lazy val commonSettings = Seq(
  organization := "edu.sfu.cs",
  name := "dandelion-sim",
  version      := "1.0-SNAPSHOT",
  scalaVersion := "2.12.10",
  parallelExecution in Global := false,
  logBuffered in Test := false,
  testOptions in Test += Tests.Argument("-oDS"),
  traceLevel   := 15,
  scalacOptions ++= Seq("-deprecation","-unchecked","-Xsource:2.11"),
  libraryDependencies ++= Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value),
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

lazy val `api-config-chipsalliance` = (project in file("api-config-chipsalliance/build-rules/sbt"))
  .settings(commonSettings)
  .settings(publishArtifact := false)
lazy val dandelion = dependOnChisel(project).settings(commonSettings)
  .settings(publishArtifact := false)
lazy val `dandelionsim` = dependOnChisel(project in file("."))
  .settings(commonSettings, chipSettings)
  .dependsOn(`api-config-chipsalliance` % "compile-internal;test-internal")
  .dependsOn(dandelion % "compile-internal;test-internal")
  .settings(
      aggregate := false,
      // Include macro classes, resources, and sources in main jar.
      mappings in (Compile, packageBin) ++= (mappings in (`api-config-chipsalliance`, Compile, packageBin)).value,
      mappings in (Compile, packageSrc) ++= (mappings in (`api-config-chipsalliance`, Compile, packageSrc)).value,
      mappings in (Compile, packageBin) ++= (mappings in (dandelion, Compile, packageBin)).value,
      mappings in (Compile, packageSrc) ++= (mappings in (dandelion, Compile, packageSrc)).value,
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
  mainClass in (Compile, run) := Some("dandelionsim.Generator"),
  make := {
    val jobs = java.lang.Runtime.getRuntime.availableProcessors
    val (makeDir, target) = setMake.parsed
    (run in Compile).evaluated
    s"make -C $makeDir  -j $jobs $target".!
  }
)





// name := "accel"
// version := "0.1.0-SNAPSHOT"
// organization := "edu.sfu.cs"

// def scalacOptionsVersion(scalaVersion: String): Seq[String] = {
//   Seq() ++ {
//     // If we're building with Scala > 2.11, enable the compile option
//     //  switch to support our anonymous Bundle definitions:
//     //  https://github.com/scala/bug/issues/10047
//     CrossVersion.partialVersion(scalaVersion) match {
//       case Some((2, scalaMajor: Long)) if scalaMajor < 12 => Seq()
//       case _ => Seq(
//         "-Xsource:2.11",
//         "-language:reflectiveCalls",
//         "-language:implicitConversions",
//         "-deprecation",
//         "-Xlint",
//         "-Ywarn-unused",
//       )
//     }
//   }
// }

// def javacOptionsVersion(scalaVersion: String): Seq[String] = {
//   Seq() ++ {
//     // Scala 2.12 requires Java 8. We continue to generate
//     //  Java 7 compatible code for Scala 2.11
//     //  for compatibility with old clients.
//     CrossVersion.partialVersion(scalaVersion) match {
//       case Some((2, scalaMajor: Long)) if scalaMajor < 12 =>
//         Seq("-source", "1.7", "-target", "1.7")
//       case _ =>
//         Seq("-source", "1.8", "-target", "1.8")
//     }
//   }
// }

// scalaVersion := "2.12.10"
// crossScalaVersions := Seq("2.12.10","2.12.8", "2.11.12")

// resolvers ++= Seq(
//   Resolver.sonatypeRepo("snapshots"),
//   Resolver.sonatypeRepo("releases"))

// libraryDependencies ++= Seq(
//   "edu.berkeley.cs" %% "chisel3" % "3.3-SNAPSHOT",
//   "edu.sfu.cs" %% "dandelion" % "1.0-SNAPSHOT"
// )

// scalacOptions ++= scalacOptionsVersion(scalaVersion.value)
// javacOptions ++= javacOptionsVersion(scalaVersion.value)
