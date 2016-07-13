
lazy val root = (project in file(".")).
  settings(
    name := "FuriousCyclist",
    version := "1.0",
    scalaVersion := "2.11.7"
  ).enablePlugins(SbtTwirl)

resolvers += "spray repo" at "http://repo.spray.io"
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/maven-releases/"
resolvers += "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"

libraryDependencies += "io.undertow" % "undertow-core" % "1.3.12.Final"

libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.13"
libraryDependencies += "com.typesafe" % "config" % "1.2.1"
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.38"
libraryDependencies += "net.codingwell" % "scala-guice_2.11" % "4.0.1"
libraryDependencies += "io.spray" % "spray-json_2.11" % "1.3.2"
libraryDependencies += "io.spray" % "spray-routing_2.11" % "1.3.3"
libraryDependencies += "io.spray" % "spray-can_2.11" % "1.3.3"
libraryDependencies += "commons-io" % "commons-io" % "2.4"
libraryDependencies += "net.debasishg" % "redisclient_2.11" % "3.1"

//set mainClass in Revolver.reStart := Some("in.lambda_hc.furious_cyclist.ServerBootstrap")