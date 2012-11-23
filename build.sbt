import AssemblyKeys._

assemblySettings

name := "bfscala"

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "1.8" % "test",
    "commons-io" % "commons-io" % "2.4",
    "org.fusesource.scalate" % "scalate-core" % "1.5.3"
)

scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation"
)
