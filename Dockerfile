

# ============================================================
# Stage 2: 运行阶段 - 仅保留 JRE + JAR，镜像更小
# ============================================================
# JDK 25 LTS 与 Spring Boot 4.x 对齐
FROM eclipse-temurin:25-jre
WORKDIR /app

# 设置时区为东八区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 从构建阶段提取 fat JAR
COPY --from=build /app/start/target/start-*.jar app.jar

# 暴露应用端口
EXPOSE 8080
# 232323 自己的项目用这个，

# JVM 参数（可通过 docker run -e JAVA_OPTS="..." 覆盖）
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
