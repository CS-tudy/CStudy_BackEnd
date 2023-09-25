FROM openjdk:11

# JAR 파일의 경로를 Docker 이미지 내부로 복사
COPY module-api/build/libs/module-api-0.0.1-SNAPSHOT.jar /app.jar

# 환경 변수 설정 (옵션)
ENV TZ=Asia/Seoul

# 애플리케이션 실행 명령
ENTRYPOINT ["java", "-jar", "/app.jar"]