FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew installDist

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/install/app .
CMD ["./bin/app"]