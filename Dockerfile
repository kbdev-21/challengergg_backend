# ==========================
# 1️⃣ Build stage
# ==========================
FROM gradle:8.3-jdk17 AS build

# Thư mục làm việc trong container
WORKDIR /app

# Copy toàn bộ source code vào container
COPY . .

# Build Spring Boot jar (Gradle)
RUN gradle bootJar --no-daemon

# ==========================
# 2️⃣ Run stage
# ==========================
FROM eclipse-temurin:17-jdk-jammy

# Thư mục làm việc cho run stage
WORKDIR /app

# Copy jar từ build stage sang
COPY --from=build /app/build/libs/*.jar app.jar

# Container lắng nghe cổng 666
EXPOSE 666

# Chạy Spring Boot jar
ENTRYPOINT ["java", "-jar", "app.jar"]