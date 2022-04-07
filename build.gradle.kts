plugins {
  id("application")
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

val YOUR_NAME = "YOUR_NAME_HERE"

repositories {
  mavenCentral()
}

application {
  mainClass.set("csc22100.spelling.SpellCheckerMain")
}

val slf4jVersion = "1.7.36"

dependencies {
  // https://mvnrepository.com/artifact/com.google.guava/guava
  implementation("com.google.guava:guava:31.1-jre")
  // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
  implementation("org.apache.commons:commons-lang3:3.12.0")

  implementation("org.slf4j:slf4j-api:$slf4jVersion")
  implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2")
  implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.2")

  implementation("info.picocli:picocli:4.6.3")
  annotationProcessor("info.picocli:picocli-codegen:4.6.3")

  runtimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")

  testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
  testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
  testImplementation("org.assertj:assertj-core:3.22.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter")
}

val dataDir = project.projectDir.resolve("data").toString()

val downloadTask by tasks.registering(JavaExec::class) {
  classpath = sourceSets.main.get().runtimeClasspath

  mainClass.set("csc22100.spelling.SpellCheckerMain")

  args = listOf("download", "--output-dir", dataDir)
}

val prepareTask by tasks.registering(JavaExec::class) {
  classpath = sourceSets.main.get().runtimeClasspath

  mainClass.set("csc22100.spelling.SpellCheckerMain")

  args = listOf("prepare", "--working-dir", dataDir)
}

val trainTask by tasks.registering(JavaExec::class) {
  classpath = sourceSets.main.get().runtimeClasspath

  mainClass.set("csc22100.spelling.SpellCheckerMain")

  args = listOf("train", "--working-dir", dataDir)
}

val evaluateTask by tasks.registering(JavaExec::class) {
  classpath = sourceSets.main.get().runtimeClasspath

  mainClass.set("csc22100.spelling.SpellCheckerMain")

  args = listOf("evaluate", "--working-dir", dataDir)
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.test {
  useJUnitPlatform {
    excludeTags("manual")
  }
}

val testAllTask by tasks.registering(Test::class) {
  testLogging {
    events("passed", "failed")

    showStandardStreams = false
  }

  systemProperty("model.path", "$dataDir/model.json")
  systemProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug")
}

tasks.build {
  dependsOn(testAllTask)
}

tasks.compileJava {
  options.compilerArgs.add("-Aproject=${project.group}/${project.name}")
  options.compilerArgs.add("-Adisable.proxy.config")
  options.compilerArgs.add("-Adisable.resource.config")
  options.compilerArgs.add("-Adisable.reflect.config")
}

tasks.register<Zip>("packageAssignment") {
  archiveFileName.set("${project.name}-$YOUR_NAME.zip")
  from(layout.projectDirectory) {
    include("*.gradle.kts")
    include("src/**")
    include("gradle/**")
    include("gradlew*")
  }

  outputs.upToDateWhen { false }

  finalizedBy(tasks.getByPath("copyZip"))
}

tasks.register<Copy>("copyZip") {
  from(tasks.getByName<Zip>("packageAssignment").outputs)

  destinationDir = File(rootProject.projectDir.toString())
}

tasks.register<JavaExec>("source2pdf") {
  classpath = files("${rootProject.projectDir}/source2pdf-all.jar")

  args = listOf("src", "-o", "${rootProject.projectDir}/$YOUR_NAME.pdf")
}