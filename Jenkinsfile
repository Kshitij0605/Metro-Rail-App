pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "metro-app"
        MYSQL_IMAGE = "mysql:8"
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
                script {
                    docker.build(DOCKER_IMAGE, '.')
                }
            }
        }

        stage('Start Services with Docker Compose') {
            steps {
                sh 'docker-compose -f ${COMPOSE_FILE} up -d'
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
                script {
                    // Assuming your app exposes endpoints on port 8080
                    sh 'sleep 10'  // Give app a moment to fully boot up
                    sh 'curl -f http://localhost:8080/api/stations'
                }
            }
        }

        stage('Tear Down') {
            steps {
                sh 'docker-compose -f ${COMPOSE_FILE} down -v'
            }
        }
    }

    post {
        always {
            echo 'Cleaning up Docker containers...'
            sh 'docker-compose -f ${COMPOSE_FILE} down -v || true'
        }
    }
}
