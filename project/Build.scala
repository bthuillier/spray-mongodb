import sbt._
import Keys._

object Build extends Build {

  lazy val summit = Project(
    id = "spray-mongodb",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      libraryDependencies ++= dependencies
    )
  ).settings(basicSettings: _*)


  lazy val dependencies = Seq(
    "com.typesafe.akka"  %% "akka-actor"       % "2.2.4",
    "org.reactivemongo"  %% "reactivemongo"    % "0.10.0",
    "com.typesafe.play" % "play-json_2.10" % "2.3.1",
    "com.typesafe.akka"  %% "akka-slf4j"       % "2.2.4",
    "ch.qos.logback"      % "logback-classic"  % "1.0.13",
    "io.spray"            % "spray-can"        % "1.2.1",
    "io.spray"            % "spray-routing"    % "1.2.1",
    "org.specs2"         %% "specs2"           % "1.14"         % "test",
    "io.spray"            % "spray-testkit"    % "1.2.1" % "test",
    "com.typesafe.akka"  %% "akka-testkit"     % "2.2.4"        % "test",
    "com.novocode"        % "junit-interface"  % "0.7"          % "test->default"
  )

  lazy val compilerOptions = Seq(
    "-unchecked",
    "-deprecation",
    "-Xlint",
    "-Ywarn-dead-code",
    "-language:_",
    "-encoding", "UTF-8"
  )

  lazy val options = Tests.Argument(TestFrameworks.JUnit, "-v")


  lazy val dependenciesResolvers = Seq(
    "spray repo" at "http://repo.spray.io",
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
  )

  lazy val basicSettings = Seq(
    organization          := "com.gilwath",
    startYear             := Some(2014),
    scalaVersion          := "2.10.4",
    version               := "0.0.0",
    resolvers             ++= dependenciesResolvers,
    autoCompilerPlugins := true,
    autoScalaLibrary := false,
    checksums in update := Nil,
    scalacOptions ++= compilerOptions,
    testOptions ++= Seq(options)
  )
}
