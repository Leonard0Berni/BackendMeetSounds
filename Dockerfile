FROM openjdk:17-jdk-slim
ARG JAR_FILE=backend/target/meetsounds-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} meetsounds-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "meetsounds-0.0.1-SNAPSHOT.jar"]
