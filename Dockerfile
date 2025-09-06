# 使用官方的 OpenJDK 21 基礎映像
FROM eclipse-temurin:21-jdk

# 設置工作目錄
WORKDIR /app

# 將本地生成的 JAR 文件複製到容器
COPY target/*.jar app.jar

# 指定運行 JAR 應用程序的命令
ENTRYPOINT ["java", "-jar", "app.jar"]
