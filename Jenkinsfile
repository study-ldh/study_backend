pipeline {
    agent any

    environment {
        // 도커 허브 계정명 (고정)
        DOCKER_HUB_ID = 'fasfaff'
        IMAGE_NAME = 'wms-backend'

        // EC2 접속 정보 (고정)
        EC2_IP = '172.31.46.63'
        EC2_USER = 'ubuntu'

        // 젠킨스 금고에서 불러올 별명들 (방금 확인한 것!)
        SSH_CRED_ID = 'back-study-ssh-key'
        DOCKER_CRED_ID = 'back_study'   // 젠킨스에 새로 등록할 이름!
    }

    stages {
        stage('Checkout') {
            steps {
                // 본인의 깃허브 주소로 바꿔주세요
                git branch: 'main', url: 'git@github.com:study-ldh/study_backend.git'
            }
        }

        stage('Build & Push with Jib') {
            steps {
                // 젠킨스 금고(DOCKER_CRED_ID)에서 ID/PW를 꺼내와서 Jib로 쏘는 단계
                withCredentials([usernamePassword(credentialsId: "${DOCKER_CRED_ID}", passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    sh "chmod +x gradlew"
                    sh "./gradlew jib -Djib.to.auth.username=${DOCKER_USERNAME} -Djib.to.auth.password=${DOCKER_PASSWORD}"
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                // 젠킨스 금고(SSH_CRED_ID)에서 키를 꺼내서 EC2에 접속하는 단계
                sshagent(credentials: ["${SSH_CRED_ID}"]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_IP} "
                            docker pull ${DOCKER_HUB_ID}/${IMAGE_NAME}:latest &&
                            docker rm -f ${IMAGE_NAME} || true &&
                            docker run -d -p 8080:8080 --name ${IMAGE_NAME} \
                              --link wms-mysql \
                              -e DB_HOST=wms-mysql \
                              -e FRONTEND_URL=http://${EC2_IP}:3000 \
                              ${DOCKER_HUB_ID}/${IMAGE_NAME}:latest
                        "
                    """
                }
            }
        }
    }
}