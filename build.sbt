name := """panda"""
organization := "chainhaus"

version := "1.0-SNAPSHOT"

//lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.7"

libraryDependencies += guice

// https://mvnrepository.com/artifact/javax.persistence/persistence-api
libraryDependencies += "javax.persistence" % "persistence-api" % "1.0.2"

// https://mvnrepository.com/artifact/be.objectify/deadbolt-java
libraryDependencies += "be.objectify" %% "deadbolt-java" % "2.6.4"

// https://mvnrepository.com/artifact/be.objectify/deadbolt-java-gs
libraryDependencies += "be.objectify" % "deadbolt-java-gs_2.12" % "2.6.0"

// https://mvnrepository.com/artifact/com.googlecode.libphonenumber/libphonenumber - Used in BaseUser
libraryDependencies += "com.googlecode.libphonenumber" % "libphonenumber" % "8.8.6"

// JavaMail for email address validity check
libraryDependencies += "javax.mail" % "mail" % "1.4.7"

libraryDependencies += "com.h2database" % "h2" % "1.4.197"

// https://mvnrepository.com/artifact/com.auth0/java-jwt
libraryDependencies += "com.auth0" % "java-jwt" % "3.3.0"

libraryDependencies += "org.projectlombok" % "lombok" % "1.18.4" % "provided"
