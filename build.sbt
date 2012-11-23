import AssemblyKeys._

assemblySettings

name := "bfscala"

resolvers += "sonatype-public" at "https://oss.sonatype.org/content/groups/public"

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "1.8" % "test",
    "commons-io" % "commons-io" % "2.4",
    "com.github.scopt" %% "scopt" % "2.1.0"
)

scalacOptions ++= Seq(
    "-unchecked",
    "-deprecation"
)
