plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.2.2" // ✅ Совместимая версия Spring Boot
    id("io.spring.dependency-management") version "1.1.4" // ✅ Версия BOM
}

group = "com.point"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // ✅ BOM для Spring Cloud, чтобы не указывать версию вручную
    implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2023.0.0"))

    // ✅ Используем WebFlux вместо Web (Spring Cloud Gateway требует WebFlux)
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

    // ✅ Поддержка JWT (Nimbus)
    implementation("com.nimbusds:nimbus-jose-jwt:9.31")

    // ✅ Поддержка Jackson для Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // ✅ Kotlin reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // ✅ Тестирование
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
