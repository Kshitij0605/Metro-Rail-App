pipeline {
    agent any

    environment {
        COMPOSE_FILE = "docker-compose.yml"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t metro-app .'
            }
        }

        stage('Start Services with Docker Compose') {
            steps {
                sh 'docker compose -f docker-compose.yml up -d'
            }
        }

        stage('Wait for MySQL to be Healthy') {
            steps {
                script {
                    timeout(time: 90, unit: 'SECONDS') {
                        waitUntil {
                            def status = sh(script: "docker inspect --format='{{.State.Health.Status}}' metro-mysql", returnStdout: true).trim()
                            return (status == "healthy")
                        }
                    }
                }
            }
        }

        stage('Run Integration Tests') {
            steps {
                sh 'sleep 10'
                sh 'curl -f http://localhost:8080/api/stations'
            }
        }

        stage('Tear Down') {
            steps {
                sh 'docker compose -f docker-compose.yml down -v'
            }
        }
    }

    post {
        always {
            echo 'Cleaning up Docker containers...'
            sh 'docker compose -f docker-compose.yml down -v || true'
        }
    }
}
