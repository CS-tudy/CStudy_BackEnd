pipeline {
    agent any
    stages {
       stage('Prepare') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/CStudyTeam/CStudy-backend.git'
            }
        }
		stage('Build Gradle') {
            steps{
                sh 'chmod +x gradlew'
                sh  './gradlew clean build'

                sh 'ls -al ./build'
            }
        }

		stage('Docker build image') {
            steps{
                sh 'docker build -t kimmugeon/docker-jenkins-github-cstudy .'
            }
        }

		stage('Docker push image') {
            steps{
                sh 'docker login -u {id} -p {password}'
                sh 'docker push kimmugeon/docker-jenkins-github-cstudy'
            }
        }

        stage('Run Container on SSH Dev Server'){
            steps{
                echo 'SSH'
                sshagent (credentials: ['dockerHubPwd']) {
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@13.209.121.180 'whoami'"
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@13.209.121.180 'docker ps -q --filter name=docker-jenkins-github-cstudy | grep -q . && docker rm -f \$(docker ps -aq --filter name=docker-jenkins-github-cstudy)'"
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@13.209.121.180 'docker rmi -f kimmugeon/docker-jenkins-github-cstudy'"
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@13.209.121.180 'docker run -d --name docker-jenkins-github-test -p 8081:8080 kimmugeon/docker-jenkins-github-cstudy'"
                }

            }

        }
    }
}
