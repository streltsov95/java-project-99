plugins {
	application
	checkstyle
	jacoco
	id("org.springframework.boot") version "3.5.3" //add spring commands
	id("io.spring.dependency-management") version "1.1.7" //apply spring boot version for packages
	id("org.sonarqube") version "6.3.1.5724"
	id("io.freefair.lombok") version "9.0.0"
	id("io.sentry.jvm.gradle") version "6.0.0"
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
	}
}

sentry {
	includeSourceContext = true

	org = "self-learning-vm"
	projectName = "java-spring-boot"
	authToken = System.getenv("SENTRY_AUTH_TOKEN")
}

repositories {
	mavenCentral()
}

dependencies {
	runtimeOnly("com.h2database:h2") //db for dev
	runtimeOnly("org.postgresql:postgresql") //db for prod

	implementation("org.springframework.boot:spring-boot-starter") //base spring package
	implementation("org.springframework.boot:spring-boot-starter-web") //for http interaction
	implementation("org.springframework.boot:spring-boot-devtools") //apply changes while app run
	implementation("org.springframework.boot:spring-boot-starter-validation") //validation package
	implementation("org.springframework.boot:spring-boot-starter-data-jpa") //ineraction with db
	implementation("org.springframework.boot:spring-boot-starter-security") //authentication package
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server") //access checking package

	testImplementation(platform("org.junit:junit-bom:5.13.4"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testImplementation("org.instancio:instancio-junit:5.5.1")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:4.1.1")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	testImplementation("org.springframework.security:spring-security-test") //authentication testing package
	testImplementation("org.springframework.boot:spring-boot-starter-test") //testing package

	implementation("org.openapitools:jackson-databind-nullable:0.2.6") //lib for partial model update
	implementation("org.mapstruct:mapstruct:1.6.3") //mapper for convert model to dto and back
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3") //annotation handler for mapper code generating
	implementation("net.datafaker:datafaker:2.5.0")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.15")
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(true)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}
