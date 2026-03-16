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

        stage('Build Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker compose -f docker-compose.yml build'
            }
        }

        stage('Run Containers') {
            steps {
                sh 'docker compose -f docker-compose.yml up -d'
            }
        }

    }
}