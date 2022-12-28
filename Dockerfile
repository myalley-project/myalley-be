FROM openjdk:11-slim as builder
WORKDIR /usr/src/app
COPY . .
RUN ./gradlew build

FROM openjdk:11-jre-slim
WORKDIR /usr/src/app
COPY --from=builder /usr/src/app/build/libs .
CMD ["java", "-jar", "./myalley.jar"]