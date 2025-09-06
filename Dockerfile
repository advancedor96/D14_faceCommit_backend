# 使用官方的 OpenJDK 21 基礎映像
# FROM eclipse-temurin:21-jdk

# 使用 Maven 官方映像進行構建
FROM maven:3.9.2-eclipse-temurin-21 AS build

# 將專案所有文件複製到容器中
COPY . /app
WORKDIR /app

# 使用 Maven 打包 Spring Boot 應用
RUN ./mvnw clean package -DskipTests

# 使用最小化的 JDK 基礎映像運行應用
FROM eclipse-temurin:21-jdk


WORKDIR /app

# 從構建階段提取 JAR 文件
COPY --from=build /app/target/*.jar app.jar

# 指定運行 JAR 應用程序的命令
ENTRYPOINT ["java", "-jar", "app.jar"]
