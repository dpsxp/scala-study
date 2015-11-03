import sbt._
import Keys._
import org.scalatra.sbt._
import com.mojolly.scalate.ScalatePlugin._
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import ScalateKeys._

object MyScalatraWebAppBuild extends Build {
  val Organization = "com.example"
  val Name = "My Scalatra Web App"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.6"
  val ScalatraVersion = "2.3.0"

  lazy val project = Project (
    "my-scalatra-web-app",
    file("."),
    settings = ScalatraPlugin.scalatraSettings ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
        "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test",
        "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "9.2.10.v20150310" % "compile;container",
        "org.eclipse.jetty" % "jetty-plus" % "9.2.10.v20150310" % "compile;container",
        "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
        "org.scalatra" %% "scalatra-json" % "2.4.0.RC1",
        "org.json4s"   %% "json4s-jackson" % "3.3.0.RC1",
        "org.mongodb" %% "casbah" % "2.7.2",
        "org.json4s" %% "json4s-mongo" % "3.2.10"
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      }
    )
  ).enablePlugins(JavaAppPackaging)
}
