
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test"
    )