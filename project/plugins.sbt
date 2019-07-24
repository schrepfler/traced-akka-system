resolvers += Resolver.bintrayIvyRepo("kamon-io", "sbt-plugins")
resolvers += Resolver.mavenCentral
addSbtPlugin("io.kamon" % "sbt-kanela-runner" % "2.0.0-RC1")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.4.0")
addSbtPlugin("io.shiftleft" % "sbt-ci-release-early" % "1.0.18")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.0.0")
addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.5")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.25")

libraryDependencies += "net.bytebuddy" % "byte-buddy-agent" % "1.9.14"
