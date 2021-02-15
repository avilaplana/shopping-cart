ThisBuild / scalaVersion := "2.13.4"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "uk.gov.hmrc"
ThisBuild / organizationName := "hmrc"

lazy val scalaTestVersion           = "3.2.2"
lazy val catsVersion                = "2.1.1"
lazy val scalaCheckVersion          = "3.2.2.0"
lazy val scalaCheckShapelessVersion = "1.2.3"

val dependencies = Seq(
  "org.typelevel"              %% "cats-core"                 % catsVersion,
  "org.scalatest"              %% "scalatest"                 % scalaTestVersion % Test,
  "org.scalatestplus"          %% "scalacheck-1-14"           % scalaCheckVersion % Test,
  "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % scalaCheckShapelessVersion % Test
)

lazy val root = (project in file("."))
  .settings(
    name := "shopping-cart",
    libraryDependencies ++= dependencies
  )
