plugins {
    autowire(libs.plugins.kotlin.jvm)
    autowire(libs.plugins.kotlin.compose)
    autowire(libs.plugins.jetbrains.compose)
}

group = property.project.samples.demo.jvm.groupName

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

compose.desktop {
    application {
        mainClass = "$group.MainKt"
    }
}

dependencies {
    implementation(projects.yukireflectionCore)
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
}