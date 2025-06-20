.DEFAULT_GOAL := build-run

setup:
	./gradlew wrapper --gradle-version 8.14.2
	./gradlew build

clean:
	./gradlew clean

build:
	./gradlew clean build

install:
	./gradlew clean installDist

#run-dist:
#	./build/install/app/bin/app

test:
	./gradlew test

#report:
#	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain

#check-deps:
#	./gradlew dependencyUpdates -Drevision=release

.PHONY: build