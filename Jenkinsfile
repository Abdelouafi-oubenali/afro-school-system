pipeline {
    agent any

    stages {

        stage('Clone Repo') {
            steps {
                git 'git@github.com:Abdelouafi-oubenali/afro-school-system.git'
            }
        }

        stage('Build Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker compose build'
            }
        }

        stage('Run Containers') {
            steps {
                sh 'docker compose up -d'
            }
        }

    }
}