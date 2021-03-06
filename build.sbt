name := "pilgrim_cms"

version := "0.1"

scalaVersion := "2.12.5"

lazy val root = (project in file("."))
  .settings(Coverage.Settings)
  .settings(inConfig(Gatling)(Defaults.testSettings): _*)

enablePlugins(GatlingPlugin)

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-Xlint")

assemblyOutputPath in assembly := file("./pilgrim-crm.jar")

scalaSource in Gatling := sourceDirectory.value / "stress_test" / "scala"

libraryDependencies ++= Seq(
  // "com.twitter" %% "finagle-mysql" % "18.3.0",
  "com.github.finagle" %% "finch-core" % "0.17.0",
  "com.github.finagle" %% "finch-argonaut" % "0.17.0",
  "com.twitter" %% "finagle-http" % "18.3.0",
  "com.github.finagle" %% "finch-circe" % "0.17.0",

  "io.circe" %% "circe-core" % "0.9.1",
  "io.circe" %% "circe-generic" % "0.9.1",
  "io.circe" %% "circe-parser" % "0.9.1",

  "org.scalaz" %% "scalaz-core" % "7.2.8",
  "org.scaldi" %% "scaldi"      % "0.5.8",

  "org.mongodb.scala" %% "mongo-scala-driver" % "2.2.+",

  "ch.qos.logback"  %  "logback-classic"  % "1.1.3",

  "org.scalactic" %% "scalactic" % "3.0.1" % "test",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test",
  "junit" % "junit" % "4.12" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "test",
  "org.mockito" % "mockito-core" % "2.10.0" % "test",
  "com.github.julien-truffaut" %% "monocle-core" % "1.5.0" % "test",
  "com.github.julien-truffaut" %% "monocle-macro" % "1.5.0" % "test",
  "com.github.julien-truffaut" %% "monocle-law" % "1.5.0" % "test",

  "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.3.1" % "test",
  "io.gatling"            % "gatling-test-framework"    % "2.3.0" % "test"
)

// deduplicate: different file contents found in the following: のエラーが出る対応
assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".xml" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".types" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".class" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

Revolver.settings

SettingKey[Unit]("scalafmtGenerateConfig") :=
  IO.write( // writes to file once when build is loaded
    file(".scalafmt.conf"),
    """style = IntelliJ
      |# Your configuration here
      |project.git = true
      |project.excludeFilters = ["target/"]
      |rewrite.rules = [ExpandImportSelectors]
    """.stripMargin.getBytes("UTF-8")
  )
