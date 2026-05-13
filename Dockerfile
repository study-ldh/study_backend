# 1. 실행 환경 설정 (우분투 서버와 동일한 OpenJDK 17)
FROM amazoncorretto:17-alpine

RUN mkdir /app
# 2. 컨테이너 내 작업 디렉토리
WORKDIR /app

ADD ./build/libs/*.jar /app/app.jar

EXPOSE 8888

# 5. 실행
ENTRYPOINT ["java", "-jar", "app.jar"]