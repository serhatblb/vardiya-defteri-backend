# 1. Aşama: Maven ile derleme
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# BURASI ÖNEMLİ: Kodlamayı UTF-8'e zorlayarak build alıyoruz
ENV MAVEN_OPTS="-Dfile.encoding=UTF-8"
RUN mvn clean package -DskipTests -Dproject.build.sourceEncoding=UTF-8

# 2. Aşama: Çalıştırma
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]