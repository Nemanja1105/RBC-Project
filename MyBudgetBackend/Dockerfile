FROM maven:3.9.8 AS builder
WORKDIR /app
COPY /MyBudgetBackend/pom.xml .
RUN mvn dependency:go-offline

COPY /MyBudgetBackend/src/ ./src/
RUN mvn clean package

FROM openjdk:17-jdk-slim
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

