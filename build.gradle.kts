plugins {
    kotlin("jvm") version "1.7.0"
}

group = "dev.avlwx"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
}