plugins {
    id("java")
}

group = "it.disi.unitn.imggen.lasagna"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:24.0.1")
    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")

    implementation("ai.djl:api:0.21.0")
    implementation(platform("ai.djl:bom:0.21.0"))
    implementation("ai.djl.pytorch:pytorch-model-zoo:0.21.0")
    runtimeOnly("ai.djl.pytorch:pytorch-jni:1.13.1-0.21.0")
    runtimeOnly("ai.djl.pytorch:pytorch-native-cu117:1.13.1")
}

tasks.test {
    useJUnitPlatform()
}