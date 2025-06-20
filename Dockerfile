FROM eclipse-temurin:21-jdk

WORKDIR /

COPY / .

RUN ./gradlew installDist

CMD ./build/install/app/bin/app