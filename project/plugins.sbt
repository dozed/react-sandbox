logLevel := Level.Warn

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.5")

resolvers += Resolver.url("GitHub repository", url("http://shaggyyeti.github.io/releases"))(Resolver.ivyStylePatterns)

addSbtPlugin("default" % "sbt-sass" % "0.1.9")
