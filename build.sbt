lazy val root = (project in file("."))
  .enablePlugins(PlayScala, PlayNettyServer)
  .disablePlugins(PlayAkkaHttpServer)
  .settings(
    name := """play-scala-compile-di-example""",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.13.1",
    libraryDependencies ++= Seq(),
    scalacOptions ++= List(
      "-encoding", "utf8",
      "-deprecation",
      "-feature",
      "-unchecked"
    )
  )
