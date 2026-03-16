# ─────────────────────────────────────────────────────────────────
# Stage 1: 构建 Vue 前端
# ─────────────────────────────────────────────────────────────────
FROM node:20-alpine AS build-frontend

WORKDIR /app/vue-web

# 先复制 package 文件，利用 Docker 层缓存
COPY vue-web/package*.json ./
RUN npm ci

# 复制源码并构建
COPY vue-web/ ./
RUN npm run build


# ─────────────────────────────────────────────────────────────────
# Stage 2: 构建 Spring Boot 后端（含嵌入的前端静态文件）
# ─────────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS build-backend

WORKDIR /app/api

# 先复制 pom.xml 并下载依赖（利用 Docker 层缓存，源码变动时不重新下载依赖）
COPY api/pom.xml ./
RUN mvn dependency:go-offline -B -q

# 复制后端源码
COPY api/src/ ./src/

# 将前端构建产物复制到 Spring Boot 静态资源目录
# Spring Boot 会自动提供 src/main/resources/static 下的文件
COPY --from=build-frontend /app/vue-web/dist/ ./src/main/resources/static/

# 打包（-Dspring-boot.repackage.skip=false 覆盖 pom.xml 中的 skip=true，生成可执行 fat JAR）
RUN mvn package -DskipTests -Dspring-boot.repackage.skip=false -B -q


# ─────────────────────────────────────────────────────────────────
# Stage 3: 最终运行镜像（仅包含 JRE，镜像尽量精简）
# ─────────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build-backend /app/api/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
