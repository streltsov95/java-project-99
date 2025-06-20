plugins {
	application
	checkstyle
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.sonarqube") version "6.2.0.5505"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

application {
	mainClass = "hexlet.code.AppApplication"
}

sonar {
	properties {
		property("sonar.projectKey", "streltsov95_java-project-99")
		property("sonar.organization", "streltsov95")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
