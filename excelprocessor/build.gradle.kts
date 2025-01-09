plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2") // Убедитесь, что версия актуальна
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")        // Основной модуль
    implementation("org.apache.poi:poi-ooxml:5.2.3")

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}