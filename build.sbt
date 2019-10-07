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
    name := "Traced Akka System",
    Compile / unmanagedSourceDirectories := Seq.empty,
    Test / unmanagedSourceDirectories := Seq.empty,
    publishArtifact := false
  )
//  .enablePlugins(AutomateHeaderPlugin, GitVersioning)

lazy val userService = (project in file("./user-service"))
  //  .enablePlugins(JavaAgent, JavaAppPackaging, UniversalPlugin)
  .enablePlugins(JavaAppPackaging, JavaAgent, DockerPlugin)
  .settings(settings)
  .settings(
    name := "User Service",
    javaAgents += "io.kamon" % "kanela-agent" % "1.0.0",
    libraryDependencies ++= Seq(
      library.akkaHttp,
      library.akkaHttpSprayJson,
      library.akkaHttpXml,
      library.akkaStream,
      library.akkaSlf4j,
      library.logback,

      library.kamonBundle,
      library.kamonZipkin,

      library.akkaHttpTestKit % Test,
      library.akkaTestKit % Test,
      library.akkaStreamTestKit % Test
    )
  )

lazy val externalUserService = (project in file("./external-user-service"))
  //  .enablePlugins(JavaAgent, JavaAppPackaging, UniversalPlugin)
  .enablePlugins(JavaAppPackaging, JavaAgent, DockerPlugin)
  .settings(settings)
  .settings(
    name := "External User Service",
    javaAgents += "io.kamon" % "kanela-agent" % "1.0.0",
    libraryDependencies ++= Seq(
      library.akkaHttp,
      library.akkaHttpSprayJson,
      library.akkaHttpXml,
      library.akkaStream,
      library.akkaSlf4j,
      library.logback,

      library.kamonBundle,
      library.kamonZipkin,

      library.akkaHttpTestKit % Test,
      library.akkaTestKit % Test,
      library.akkaStreamTestKit % Test
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
      //      library.cornichonKafka % Test,
      //      library.cornichonCheck % Test,
      //      library.cornichonHttpMock % Test,
      //      library.cornichonScalaTest % Test,
      //      library.cornichonTestFramework % Test,
      library.scalaTest % Test,
      library.logback % Test,
      library.slf4j % Test,
      library.requests % Test,
      library.circeCore,
      library.circeGeneric,
      library.circeParser,
      library.akkaHttp,
      library.akkaStream,
      library.akkaSlf4j,
      "de.heikoseeberger" %% "akka-http-circe" % "1.29.1",
      library.akkaHttpTestKit % Test,
      library.akkaTestKit % Test,
      library.akkaStreamTestKit % Test
    )
    //    testFrameworks += new TestFramework("com.github.agourlay.cornichon.framework.CornichonFramework")
  )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library = new {

  object v {
    val testContainersScala = "0.33.0"
    val akka = "2.5.25"
    val akkaHttp = "10.1.10"
    val argonaut = "6.2.3"
    val avro4s = "1.9.0"
    val circe = "0.12.1"
    val jacksonModuleScala = "2.9.9"
    val jsoniterScalaMacros = "0.51.3"
    val json4s = "3.6.6"
    val play = "2.7.4"
    val upickle = "0.7.4"
    val avsystemCommons = "1.34.17"
    val testContainers = "1.12.2"
    val scalaTest = "3.0.8"
    val cornichon = "0.18.1"
    val logback = "1.2.3"
    val selenium = "2.53.1"
    val slf4j = "1.7.28"
    val kamon = "2.0.2"
    val requests = "0.2.0"
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
  lazy val kamonBundle = "io.kamon" %% "kamon-bundle" % v.kamon
  lazy val kamonZipkin = "io.kamon" %% "kamon-zipkin" % "2.0.0"
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % v.akkaHttp
  lazy val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % v.akkaHttp
  lazy val akkaHttpXml = "com.typesafe.akka" %% "akka-http-xml" % v.akkaHttp
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % v.akka
  lazy val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % v.akka
  lazy val akkaHttpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % v.akkaHttp
  lazy val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % v.akka
  lazy val akkaStreamTestKit = "com.typesafe.akka" %% "akka-stream-testkit" % v.akka
  lazy val circeCore = "io.circe" %% "circe-core" % v.circe
  lazy val circeGeneric = "io.circe" %% "circe-generic" % v.circe
  lazy val circeParser = "io.circe" %% "circe-parser" % v.circe
  lazy val requests = "com.lihaoyi" %% "requests" % v.requests
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
  scalaVersion := "2.13.1",
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
