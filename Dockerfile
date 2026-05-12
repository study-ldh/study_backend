# 1. 실행 환경 설정 (우분투 서버와 동일한 OpenJDK 17)
FROM amazoncorretto:17-alpine

# 2. 컨테이너 내 작업 디렉토리
WORKDIR /app

# 3. JAR 파일 복사
# (주의) 빌드 후 생성되는 실제 jar 파일명과 맞춰야 합니다.
COPY build/libs/*.jar app.jar

# 4. 포트 설정 (스프링 부트 기본 포트)
EXPOSE 8080

# 5. 실행
ENTRYPOINT ["java", "-jar", "app.jar"]