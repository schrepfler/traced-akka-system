inThisBuild(
  Seq(
    organization := "net.sigmalab.tas",
    homepage := Some(url("https://github.com/schrepfler/traced-akka-system/")),
    licenses += "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"),
    scmInfo := Some(
      ScmInfo(url("https://github.com/schrepfler/traced-akka-system/"),
        "git@github.com:schrepfler/traced-akka-system.git")
    ),
    developers := List(
      Developer("schrepfler",
        "Srdan Srepfler",
        "schrepfler@gmail.com",
        url("https://github.com/schrepfler"))
    ),
    resolvers ++= Seq(
      DefaultMavenRepository,
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
    )
  )
)

lazy val tracedAkkaSystem = (project in file("."))
  .aggregate(userService)
  .aggregate(externalUserService)
  .aggregate(integrationTest)
  .settings(settings)
  .settings(
    Compile / unmanagedSourceDirectories := Seq.empty,
    Test / unmanagedSourceDirectories    := Seq.empty,
    publishArtifact := false
  )
//  .enablePlugins(AutomateHeaderPlugin, GitVersioning)


lazy val akkaHttpVersion = "10.1.9"
lazy val akkaVersion = "2.5.23"

lazy val userService = (project in file("./user-service"))
  .enablePlugins(JavaAgent, JavaAppPackaging, UniversalPlugin)
  .settings(settings)
  .settings(
    name := "User Service",
    javaAgents += "io.kamon" % "kanela-agent" % "1.0.0-RC4",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,

      "io.kamon" %% "kamon-bundle" % "2.0.0",
      "io.kamon" %% "kamon-zipkin" % "2.0.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3",

      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test
    )
  )

lazy val externalUserService = (project in file("./external-user-service"))
  .enablePlugins(JavaAgent, JavaAppPackaging, UniversalPlugin)
  .settings(settings)
  .settings(
    name := "External User Service",
    javaAgents += "io.kamon" % "kanela-agent" % "1.0.0-RC4",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,

      "io.kamon" %% "kamon-bundle" % "2.0.0",
      "io.kamon" %% "kamon-zipkin" % "2.0.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3",

      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.8" % Test
    )
  )

lazy val integrationTest = (project in file("./integration-test"))
  .settings(settings)
  .settings(
    name := "Integration Test",
    libraryDependencies ++= Seq(
      library.testContainers % Test,
      library.testContainersKafka % Test,
      library.testContainersCassandra % Test,
      library.testContainersPostgreSQL % Test,
      library.testContainersSelenium % Test,
      library.selenium % Test,
      library.cornichonKafka % Test,
      library.cornichonCheck % Test,
      library.cornichonHttpMock % Test,
      library.cornichonScalaTest % Test,
//      library.cornichonTestFramework % Test, //changing(),
      library.scalaTest % Test,
      library.logback % Test,
      library.slf4j % Test
    )
//    testFrameworks += new TestFramework("com.github.agourlay.cornichon.framework.CornichonFramework")
  )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library = new {

    object v {
      val testContainersScala = "0.29.0"
      val akka = "2.5.23"
      val akkaHttp = "10.1.9"
      val argonaut = "6.2.3"
      val avro4s = "1.9.0"
      val circe = "0.11.1"
      val jacksonModuleScala = "2.9.9"
      val jsoniterScalaMacros = "0.51.3"
      val json4s = "3.6.6"
      val play = "2.7.4"
      val upickle = "0.7.4"
      val avsystemCommons = "1.34.17"
      val testContainers = "1.12.0"
      val scalaTest = "3.0.8"
      val cornichon = "0.17.2-SNAPSHOT"
      val logback = "1.2.3"
      val selenium = "2.53.1"
      val slf4j = "1.7.26"
    }

    lazy val scalaTest = "org.scalatest" %% "scalatest" % v.scalaTest
    lazy val testContainers = "com.dimafeng" %% "testcontainers-scala" % v.testContainersScala
    lazy val testContainersKafka = "org.testcontainers" % "kafka" % v.testContainers
    lazy val testContainersCassandra = "org.testcontainers" % "cassandra" % v.testContainers
    lazy val testContainersPostgreSQL = "org.testcontainers" % "postgresql" % v.testContainers
    lazy val testContainersSelenium = "org.testcontainers" % "selenium" % v.testContainers
    lazy val cornichonHttpMock = "com.github.agourlay" %% "cornichon-http-mock" % v.cornichon
    lazy val cornichonKafka = "com.github.agourlay" %% "cornichon-kafka" % v.cornichon
    lazy val cornichonCheck = "com.github.agourlay" %% "cornichon-check" % v.cornichon
    lazy val cornichonScalaTest = "com.github.agourlay" %% "cornichon-scalatest" % v.cornichon
    lazy val cornichonTestFramework = "com.github.agourlay" %% "cornichon-test-framework" % v.cornichon
    lazy val logback = "ch.qos.logback" % "logback-classic" % v.logback
    lazy val selenium = "org.seleniumhq.selenium" % "selenium-java" % v.selenium
    lazy val slf4j = "org.slf4j" % "slf4j-api" % v.slf4j

  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
  scalafmtSettings ++
  javaCompileSettings ++
  publishSettings

lazy val commonSettings = Seq(
  scalaVersion := "2.12.8",
  organizationName := "Sigmalab",
  startYear := Some(2019),
  scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation",
    "-language:_",
    "-target:jvm-1.8",
    "-encoding", "UTF-8"
  ),
  Compile / unmanagedSourceDirectories := Seq((Compile / scalaSource).value),
  Test / unmanagedSourceDirectories := Seq((Test / scalaSource).value)
)

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true
  )

lazy val javaCompileSettings = Seq(
  javacOptions ++= Seq(
    "-encoding", "UTF-8",
    "-source", "1.8",
    "-target", "1.8",
    "-Xlint:all",
    "-parameters" // See https://github.com/FasterXML/jackson-module-parameter-names
  )
)

lazy val publishSettings =
  Seq(
    pomIncludeRepository := (_ => false),
  )




// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
