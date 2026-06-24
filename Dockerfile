# ============================================================
# Stage 1: 构建阶段 — 使用 Maven Wrapper 编译打包
# ============================================================
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# 复制 Maven Wrapper（无需本地安装 Maven，版本由项目锁定）
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

# 先复制所有模块的 pom.xml（利用 Docker 层缓存：POM 不变则依赖层可复用）
COPY pom.xml .
COPY common/pom.xml common/
COPY domain/pom.xml domain/
COPY api/pom.xml api/
COPY start/pom.xml start/
COPY log/pom.xml log/
COPY watermelon/pom.xml watermelon/
COPY watermelon/administration/pom.xml watermelon/administration/
COPY watermelon/user/pom.xml watermelon/user/
COPY watermelon/sugarcane/pom.xml watermelon/sugarcane/
COPY watermelon/crow/pom.xml watermelon/crow/
COPY watermelon/pitaya/pom.xml watermelon/pitaya/

# 下载全部依赖（POM 未变时此层命中缓存，跳过重复下载）
RUN ./mvnw dependency:go-offline -B -q

# 复制源码
COPY common/src common/src/
COPY domain/src domain/src/
COPY api/src api/src/
COPY start/src start/src/
COPY log/src log/src/
COPY watermelon/administration/src watermelon/administration/src/
COPY watermelon/user/src watermelon/user/src/
COPY watermelon/sugarcane/src watermelon/sugarcane/src/
COPY watermelon/crow/src watermelon/crow/src/
COPY watermelon/pitaya/src watermelon/pitaya/src/

# 编译打包（跳过测试以加快构建）
RUN ./mvnw clean package -DskipTests -B -q

# ============================================================
# Stage 2: 运行阶段 — 仅保留 JRE + JAR，镜像更小
# ============================================================
FROM eclipse-temurin:17-jre
WORKDIR /app

# 设置时区为东八区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 从构建阶段提取 fat JAR
COPY --from=build /app/start/target/start-*.jar app.jar

# 暴露应用端口
EXPOSE 8080

# JVM 参数（可通过 docker run -e JAVA_OPTS="..." 覆盖）
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
