var fileName = "ServiceHelper"

plugins {
    java
    application
    id("org.jetbrains.kotlin.jvm") version "1.5.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.5.0"
    id("org.openjfx.javafxplugin") version "0.0.7"
    id("edu.sc.seis.launch4j") version "2.4.6"
}

group = "com.likco"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("mysql", "mysql-connector-java", "8.0.23")

    implementation("org.jsoup:jsoup:1.13.1")

    implementation("com.google.code.gson:gson:2.8.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.0")

    implementation("org.apache.poi", "poi", "5.0.0")
    implementation("org.apache.poi", "poi-ooxml", "5.0.0")

    implementation("it.sauronsoftware:junique:1.0.4")

    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("reflect"))
}

javafx {
    modules = mutableListOf("javafx.controls", "javafx.fxml")
    version = "15.0.1"
}

tasks.test {
    useTestNG()
}

launch4j {
    jreMinVersion = "1.0.0"
    headerType = "gui"
    mainClassName = "ldcapps.servicehelper.WindowsKt"
    outfile = "$fileName.exe"
    jar = "../libs/$fileName.jar"
}

tasks.compileJava {
    options.isIncremental = true
    options.isFork = true
    options.isFailOnError = false

    options.release.set(8)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks.jar {
    from(sourceSets.main.get().output)
    archiveName = "$fileName.jar"
    dependsOn(configurations.runtimeClasspath)
    manifest {
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "META-INF/*.MF")
        attributes(
            "Implementation-Title" to "ServiceHelper",
            "Implementation-Version" to archiveVersion,
            "Main-Class" to "ldcapps.servicehelper.WindowsKt",
            "Class-Path" to configurations.runtime.files.joinToString(" ") { "lib/$it.name" }
        )
    }
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    
    
}