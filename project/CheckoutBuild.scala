import sbt._
import sbt.Keys._

object CheckoutBuild extends Build {

  lazy val checkout = Project(
    id = "checkout",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "checkout",
      organization := "net.ikenna",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.0"
      // add other settings here
    )
  )
}
