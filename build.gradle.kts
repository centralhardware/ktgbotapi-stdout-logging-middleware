plugins {
    java
    `maven-publish`
    kotlin("jvm") version "2.2.10"
}

group = "me.centralhardware"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("dev.inmo:tgbotapi:28.0.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(24)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "me.centralhardware"
            artifactId = "ktgbotapi-stdout-logging-middleware"
            version = "1.0-SNAPSHOT"
            from(components["java"])
        }
    }
}
