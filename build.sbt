name := "react-sandbox"

version := "1.0"

scalaVersion := "2.11.7"

val scalaJSReactVersion = "0.10.1"

val jsOutput = file("target/web/stage/js")

persistLauncher := true

persistLauncher in Test := false

resolvers += Opts.resolver.sonatypeSnapshots

libraryDependencies ++= Seq(
  "org.json4s" %%% "json4s-ast" % "4.0.0-SNAPSHOT",
  "org.scala-js" %%% "scalajs-dom" % "0.8.2",
  "com.greencatsoft" %%% "scalajs-angular" % "0.6",
  "com.github.japgolly.scalajs-react" %%% "core" % scalaJSReactVersion,
  "com.github.japgolly.scalajs-react" %%% "extra" % scalaJSReactVersion,
  "com.github.japgolly.fork.scalaz" %%% "scalaz-core" % "7.1.2",
  "org.http4s" %% "http4s-blaze-server" % "0.10.1",
  "org.http4s" %% "http4s-dsl" % "0.10.1",
  "org.scalaz" %% "scalaz-core" % "7.1.2",
  "ch.qos.logback" % "logback-classic" % "1.0.6"
)

jsDependencies += RuntimeDOM

jsDependencies ++= Seq(
  /// Webjars dependencies
  "org.webjars.npm" % "react" % "0.14.1" / "react-with-addons.js" commonJSName "React" minified "react-with-addons.min.js",
  "org.webjars.npm" % "react-dom" % "0.14.1" / "react-dom.js" commonJSName "ReactDOM" minified "react-dom.min.js" dependsOn "react-with-addons.js"
)

// creates single js resource file for easy integration in html page
skip in packageJSDependencies := false

// copy  javascript files to js folder,that are generated using fastOptJS/fullOptJS
crossTarget in(Compile, fullOptJS) := jsOutput
crossTarget in(Compile, fastOptJS) := jsOutput
crossTarget in(Compile, packageJSDependencies) := jsOutput
crossTarget in(Compile, packageScalaJSLauncher) := jsOutput
crossTarget in(Compile, packageMinifiedJSDependencies) := jsOutput

artifactPath in(Compile, fastOptJS) := ((crossTarget in(Compile, fastOptJS)).value / ((moduleName in fastOptJS).value + "-opt.js"))

scalacOptions += "-feature"

enablePlugins(ScalaJSPlugin, SbtWeb)

addCommandAlias("dev", "~;web-stage;fastOptJS")
