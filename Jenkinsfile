pipeline {
    agent any

    stages {

        stage('Clean Workspace') {
            steps {
                deleteDir()
            }
        }

        stage('Clone Repo') {
            steps {
                git branch: 'jeckes-start', url: 'https://github.com/Abdelouafi-oubenali/afro-school-system.git'
            }
        }

        stage('Build API Gateway') {
            steps {
                dir('api-gatewa') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Class Service') {
            steps {
                dir('class-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Message Service') {
            steps {
                dir('message-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Note Service') {
            steps {
                dir('note-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build User Service') {
            steps {
                dir('user-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Eureka Server') {
            steps {
                dir('eureka-server') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker build -t eureka-server:latest ./eureka-server'
                sh 'docker build -t api-gateway:latest ./api-gatewa'
                sh 'docker build -t user-service:latest ./user-service'
                sh 'docker build -t class-service:latest ./class-service'
                sh 'docker build -t note-service:latest ./note-service'
                sh 'docker build -t message-notification-service:latest ./message-service'
            }
        }

        stage('Run Containers') {
            steps {
                sh 'docker compose -f docker-compose.yml up -d postgres-user'
                sh 'docker compose -f docker-compose.yml up -d postgres-class'
                sh 'docker compose -f docker-compose.yml up -d postgres-note'
                sh 'docker compose -f docker-compose.yml up -d postgres-message'
                sh 'docker compose -f docker-compose.yml up -d pgadmin'
                sh 'docker compose -f docker-compose.yml up -d eureka-server'
                sh 'docker compose -f docker-compose.yml up -d api-gateway'
                sh 'docker compose -f docker-compose.yml up -d user-service'
                sh 'docker compose -f docker-compose.yml up -d class-service'
                sh 'docker compose -f docker-compose.yml up -d note-service'
                sh 'docker compose -f docker-compose.yml up -d message-notification-service'
            }
        }
    }
}


