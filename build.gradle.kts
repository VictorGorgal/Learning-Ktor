
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    // Core
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.5"

    // Serialization
    kotlin("plugin.serialization") version "1.9.10"
}

group = ""
version = "0.0.1"

application {
    mainClass.set(".ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    // Core
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Serialization
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    // Ktorm
    // https://mvnrepository.com/artifact/org.ktorm/ktorm-core
    implementation("org.ktorm:ktorm-core:3.6.0")

    // MariaDB
    // https://mvnrepository.com/artifact/org.mariadb.jdbc/mariadb-java-client
    implementation("org.mariadb.jdbc:mariadb-java-client:3.2.0")

    // Encryption
    // https://mvnrepository.com/artifact/org.mindrot/jbcrypt
    implementation("org.mindrot:jbcrypt:0.4")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}
