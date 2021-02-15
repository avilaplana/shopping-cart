ThisBuild / scalaVersion := "2.13.4"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "uk.gov.hmrc"
ThisBuild / organizationName := "hmrc"

lazy val scalaTestVersion = "3.2.2"

val dependencies = Seq(
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test
)

lazy val root = (project in file("."))
  .settings(
    name := "shopping-cart",
    libraryDependencies ++= dependencies
  )
