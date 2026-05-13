// 1. 변수 정의 부분 (이미지와 100% 동일한 스타일)
def mainDir = "."
def ecrLoginHelper = "docker-credential-ecr-login" // 도커 허브 사용 시 참고용
def region = "ap-northeast-2"
def ecrUrl = "534883914619.dkr.ecr.ap-northeast-2.amazonaws.com"
def repository = "study"
def deployHost = "52.78.98.7"

pipeline {
    agent any

    stages {
        // Stage 1: GitHub에서 코드 가져오기
        stage('Pull Codes from Github') {
            steps {
                checkout scm
            }
        }

        // Stage 2: Gradle 빌드
        stage('Build Codes by Gradle') {
            steps {
                sh "./gradlew clean build"
            }
        }

        // Stage 3: Jib를 이용해 이미지 빌드 및 푸시
        // 이미지의 'Build Docker Image by Jib & Push to AWS ECR' 단계를 도커 허브용으로 최적화
        stage('Build Docker Image by Jib & Push to AWS ECR Repository') {
            steps {
                withAWS(region: "${region}", credentials: "aws-key") {
                    ecrLogin()
                    sh """
                        curl -0 https://amazon-ecr-credential-helper-releases.s3.us-east-2.amazonaws.com/0.4.0/linux-amd64/${ecrLoginHelper}
                        
                        cd ${mainDir}
                        ./gradlew jib -Djib.to.image=${dockerHubId}/${repository}:${currentBuild.number} -Djib.console='plain'
                    """
                }
            }
        }

        // Stage 4: 운영 서버(EC2) 배포
        stage('Deploy to AWS EC2 VM') {
            steps {
                // 이미지 하단의 sshagent 구조 반영
                sshagent(credentials: ['deploy-key']) {
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@${deployHost} \
                    'aws ecr get-login-password --region ${region} | docker login --username AWS --password-stdin ${ecrUrl}/${repository}; \
                    docker run -d -p 80:8888 -t ${ecrUrl}/${repository}:${currentBuild.number};'"
                }
            }
        }
    }
}