ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / scalacOptions += "-target:jvm-17"
ThisBuild / javacOptions ++= Seq("-source", "17", "-target", "17")

val eventValidatorVersion = "0.2.0"
val AwsSdkVersion = "2.17.168"
val scalaTestVersion = "3.2.9"
val catsVersion = "2.7.0"
val catsEffectVersion = "3.3.14"
//val scalaMockVersion = "5.1.0"


/** dataEngTools module
 *
 * This module is intended to store helpful snippets of code that are useful
 * for data engineering related tasks. This may not be the best place for this
 * to live long term but for the time being it can live here.
 */
lazy val dataEngTools = project
  .in(file("module/dataEngTools"))
  .settings(
    name := "DataEngTools",
    libraryDependencies ++= Seq(
      "software.amazon.awssdk" % "s3" % AwsSdkVersion,
      "org.typelevel" %% "cats-core" % catsVersion,
      "org.typelevel" %% "cats-effect" % catsEffectVersion,
      /** Packages to check out:
       * --- https://fs2.io/#/
       * --- https://monix.io/
       * --- "org.typelevel" %% "log4cats-slf4j" % "2.4.0",
       *
       */

      "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
      //"org.scalamock" %% "scalamock" % scalaMockVersion % Test
      ),
    testOptions ++= Seq(
      Tests.Argument(TestFrameworks.ScalaTest, "-oS")
      )
    )


lazy val s3eventValidationService = project
  .in(file("module/validationService"))
  .dependsOn(dataEngTools % "compile->compile")
  .settings(
    name := "event-stream-diagnostic",
    libraryDependencies ++= Seq(
      "com.styleseat" %% "events" % eventValidatorVersion,
      "com.styleseat" %% "eventvalidator" % eventValidatorVersion,
      )
  )


lazy val testingPlayground = project
  .in(file("module/testingPlayground"))
  .settings(
    name := "playground",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % catsEffectVersion
      )
  )
