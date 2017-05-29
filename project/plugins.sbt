logLevel := Level.Warn

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

resolvers += Resolver.url("dancingrobot84-bintray", url("http://dl.bintray.com/dancingrobot84/sbt-plugins/"))(Resolver.ivyStylePatterns)

resolvers += Resolver.url("quaich-bintray", url("https://dl.bintray.com/quaich/sbt-plugins"))(Resolver.ivyStylePatterns)

resolvers += Resolver.jcenterRepo

addSbtPlugin("com.dancingrobot84" % "sbt-idea-plugin" % "0.4.0")

addSbtPlugin("me.lessis" % "bintray-sbt" % "0.3.0")
//addSbtPlugin("org.foundweekends" % "sbt-bintray" % "0.5.0")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2")

val quartercaskVersion = "0.0.4-SNAPSHOT"

addSbtPlugin("codes.bytes" % "sbt-quartercask-lambda" % quartercaskVersion)
addSbtPlugin("codes.bytes" % "sbt-quartercask-api-gateway" % quartercaskVersion)