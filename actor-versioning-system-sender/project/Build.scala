import sbt._
import sbt.Keys._
import com.virtuslab.avs._

object Build extends Build {

  val akkaVersion = "2.3.12"
  val sprayVersion = "1.3.3"

  val tests = Seq(
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "junit" % "junit" % "4.11" % "test",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  )

  val akka = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion
  )

  val spray = Seq {
    "io.spray" %% "spray-can" % sprayVersion
  }

  val typesafe = Seq(
    Classpaths.typesafeReleases,
    Classpaths.typesafeSnapshots,
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
    "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
  )

  lazy val baseDeps = tests ++ akka ++ spray

  val appName = "avs-sender"
  val appVersion = "0.1"

  // Build definition
  lazy val buildSettings = Seq(
    organization := "com.virtuslab",
    version := appVersion,
    scalaVersion := "2.11.7",
    name := appName
  )

  // resolvers, dependencies and so on
  lazy val defaultSettings = buildSettings ++ Seq(
    libraryDependencies ++= baseDeps
  ) ++ AVSPlugin.defaultSettings

  val alsoOnTests = "compile->compile;test->test"

  lazy val root = sbt.Project(
    id = appName,
    base = file("."),
    settings = Project.defaultSettings ++ defaultSettings
  )

}
