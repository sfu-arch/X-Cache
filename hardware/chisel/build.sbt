name := "accel"
version := "0.1.0-SNAPSHOT"
organization := "edu.sfu.cs"

def scalacOptionsVersion(scalaVersion: String): Seq[String] = {
  Seq() ++ {
    // If we're building with Scala > 2.11, enable the compile option
    //  switch to support our anonymous Bundle definitions:
    //  https://github.com/scala/bug/issues/10047
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, scalaMajor: Long)) if scalaMajor < 12 => Seq()
      case _ => Seq(
        "-Xsource:2.11",
        "-language:reflectiveCalls",
        "-language:implicitConversions",
        "-deprecation",
        "-Xlint",
        "-Ywarn-unused",
      )
    }
  }
}

def javacOptionsVersion(scalaVersion: String): Seq[String] = {
  Seq() ++ {
    // Scala 2.12 requires Java 8. We continue to generate
    //  Java 7 compatible code for Scala 2.11
    //  for compatibility with old clients.
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, scalaMajor: Long)) if scalaMajor < 12 =>
        Seq("-source", "1.7", "-target", "1.7")
      case _ =>
        Seq("-source", "1.8", "-target", "1.8")
    }
  }
}

scalaVersion := "2.12.8"
crossScalaVersions := Seq("2.12.8", "2.11.12")

resolvers ++= Seq(
  Resolver.sonatypeRepo("snapshots"),
  Resolver.sonatypeRepo("releases"))

libraryDependencies ++= Seq(
  "edu.berkeley.cs" %% "chisel3" % "3.2-SNAPSHOT",
  "edu.sfu.cs" %% "dandelion-lib" % "0.1-SNAPSHOT"
)

scalacOptions ++= scalacOptionsVersion(scalaVersion.value)
javacOptions ++= javacOptionsVersion(scalaVersion.value)