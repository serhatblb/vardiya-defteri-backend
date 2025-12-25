# 1. Aşama: Maven ile derleme (Java 21 kullanıyoruz)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Kodlamayı UTF-8'e zorla ve derle
ENV MAVEN_OPTS="-Dfile.encoding=UTF-8"
RUN mvn clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8

# 2. Aşama: Çalıştırma (Java 21 kullanıyoruz)
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]