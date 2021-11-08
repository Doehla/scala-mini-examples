ThisBuild / organization := "doehla"
ThisBuild / scalaVersion := "2.13.0"

val http4sVersion = "0.23.6"
val circeVersion = "0.14.1"
val scalatestVersion = "3.2.9"
val logbackVersion = "1.2.3"
val scalaLogVersion = "3.9.4"

/*
  API example
 */
lazy val apiWithHttp4s = project
  .in(file("examples/apiWithHttp4s"))
  .settings(
    name := "apiWithHttp4s",
    version := "0.0.1",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-generic-extras" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "io.circe" %% "circe-literal" % circeVersion,
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % scalaLogVersion,
      "org.scalatest" %% "scalatest" % scalatestVersion % "test"
      ),
    testOptions ++= Seq(
      Tests.Argument(TestFrameworks.ScalaTest, "-oS")
      )
    )
