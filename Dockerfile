# 使用官方的 OpenJDK 21 基礎映像
# FROM eclipse-temurin:21-jdk

# 使用 Maven 官方映像進行構建
FROM eclipse-temurin:21-jdk


# 將專案所有文件複製到容器中
WORKDIR /app
COPY . .
RUN chmod +x mvnw


# 使用 Maven 打包 Spring Boot 應用
RUN ./mvnw clean package -DskipTests

# 將生成的 jar 檔移動到工作目錄
RUN cp target/*.jar app.jar
# 從構建階段提取 JAR 文件
EXPOSE 8080
# 指定運行 JAR 應用程序的命令
ENTRYPOINT ["java", "-jar", "app.jar"]
