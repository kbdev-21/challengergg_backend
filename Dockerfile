# ==========================
# 1️⃣ Build stage
# ==========================
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Thư mục làm việc trong container
WORKDIR /app

# Copy pom.xml trước để cache layer Maven dependencies
COPY pom.xml .

# Nếu bạn có file settings.xml hoặc cần private repo, thêm COPY ở đây
# COPY settings.xml /root/.m2/settings.xml

# Copy toàn bộ source code
COPY src ./src

# Build Spring Boot jar, skip test để nhanh hơn
RUN mvn clean package -DskipTests

# ==========================
# 2️⃣ Run stage
# ==========================
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy jar từ build stage sang
COPY --from=build /app/target/*.jar app.jar

# Container lắng nghe cổng 666
EXPOSE 666

# Chạy Spring Boot jar
ENTRYPOINT ["java", "-jar", "app.jar"]
