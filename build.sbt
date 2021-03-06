val ScalatraVersion = "2.6.3"

organization := "com.pbrandwijk"

name := "Scalatra Shopping Web App"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.6"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.8.v20171121" % "compile;container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "net.liftweb" %% "lift-json" % "3.3.0"
)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)
enablePlugins(JavaAppPackaging)
