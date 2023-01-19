FROM gradle:7-jdk11-alpine as builder

RUN gradle --version && java -version

WORKDIR /usr/src/app

COPY build.gradle .
COPY settings.gradle .
COPY gradle .

RUN gradle clean build --no-daemon > /dev/null 2>&1 || true

COPY . .

RUN gradle clean build --no-daemon --exclude-task test -i --stacktrace

FROM openjdk:11-jre-slim
WORKDIR /usr/src/app
COPY --from=builder /usr/src/app/build/libs .
CMD ["java", "-jar", "./myalley.jar"]