plugins {
    `java-library`
    `maven-publish`
    id("com.diffplug.spotless") version "8.4.0"
    id("pl.allegro.tech.build.axion-release") version "1.18.0"
}

scmVersion {
    tag {
        prefix.set("")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

group = "com.poslugator"
version = scmVersion.version

repositories {
    mavenCentral()
}

dependencies {
    implementation("tools.jackson.core:jackson-databind:3.1.2")
    implementation("tools.jackson.core:jackson-core:3.1.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            groupId = project.group.toString()
            artifactId = "poslugator-events"
            version = project.version.toString()
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

spotless {
    java {
        palantirJavaFormat("2.90.0")
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        ktlint("1.5.0")
        trimTrailingWhitespace()
        endWithNewline()
    }
}
