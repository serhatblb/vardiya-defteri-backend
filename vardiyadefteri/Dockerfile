# 1. Aşama: Uygulamayı Derle (Maven kullanıyoruz)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Testleri atlayarak build alıyoruz ki deploy hızlı olsun
RUN mvn clean package -DskipTests

# 2. Aşama: Çalıştır (Sadece gerekli dosyaları alıyoruz, boyutu küçültüyoruz)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]