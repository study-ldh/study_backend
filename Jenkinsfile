pipeline {
    agent any

    environment {
        // 1. 도커 허브 정보 (본인 계정)
        DOCKER_HUB_ID = 'fasfaff'
        IMAGE_NAME = 'wms-backend'

        // 2. AWS EC2 서버 정보
        EC2_IP = '172.31.46.63'
        EC2_USER = 'ubuntu'

        // 3. 젠킨스 금고(Credentials) 이름 - 사진에서 확인한 이름들!
        SSH_CRED_ID = 'back-study-ssh-key'
        DOCKER_CRED_ID = 'back_study'
    }

    stages {
        // [1단계] 깃허브에서 최신 코드 가져오기
        stage('Checkout') {
            steps {
                git branch: 'main',
                        credentialsId: "${SSH_CRED_ID}",
                        url: 'git@github.com:study-ldh/study_backend.git'
            }
        }

        // [2단계] 빌드하고 도커 허브에 이미지 올리기 (윈도우 전용 bat 명령어)
        stage('Build & Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_CRED_ID}", passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    // 윈도우 젠킨스이므로 bat 사용
                    bat "gradlew.bat jib -Djib.to.auth.username=%DOCKER_USERNAME% -Djib.to.auth.password=%DOCKER_PASSWORD%"
                }
            }
        }

        // [3단계] AWS 우분투 서버에 원격 접속해서 실행하기
        stage('Deploy') {
            steps {
                sshagent(credentials: ["${SSH_CRED_ID}"]) {
                    // 서버 접속 및 도커 실행 (줄바꿈 없이 한 줄로 처리하는 것이 윈도우에서 안전합니다)
                    bat """
                        ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_IP} "docker pull ${DOCKER_HUB_ID}/${IMAGE_NAME}:latest && docker rm -f ${IMAGE_NAME} || true && docker run -d -p 8080:8080 --name ${IMAGE_NAME} --link wms-mysql -e DB_HOST=wms-mysql -e FRONTEND_URL=http://${EC2_IP}:3000 ${DOCKER_HUB_ID}/${IMAGE_NAME}:latest"
                    """
                }
            }
        }
    }
}