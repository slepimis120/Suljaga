plugins {
    application
    kotlin("jvm") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.github.slepimis120.suljaga"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

application {
    mainClass.set("com.github.slepimis120.suljaga.Suljaga")
}

tasks.jar {
    enabled = false
}

tasks.shadowJar {
    manifest {
        attributes["Main-Class"] = "com.github.slepimis120.suljaga.Suljaga"
    }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}