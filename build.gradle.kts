plugins {
    kotlin("jvm") version "2.1.10"
    application
}

group = "uz.ttpu.coroutines"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("uz.ttpu.coroutines.MainKt")
}

tasks.test {
    useJUnitPlatform()
}
