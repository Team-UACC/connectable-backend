pipeline {
     agent any
     stages {
         stage('Prepare') {
             steps {
                 sh 'aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin public.ecr.aws/y3o5z1g2'
             }
         }
         stage('Gradle Build') {
             steps {
                 sh './gradlew clean build'
             }
         }
         stage('Docker Build') {
             steps {
                 sh 'docker build -t connectable .'
             }
         }
         stage('Aws ECR Upload') {
             steps {
                 sh 'docker tag connectable:latest public.ecr.aws/y3o5z1g2/connectable:latest'
                 sh 'docker push public.ecr.aws/y3o5z1g2/connectable:latest'
             }
         }
         stage('Aws ECS Deploy') {
            steps {
                sh 'aws ecs update-service --region ap-northeast-2 --cluster connectable-prod-cluster --service prod-service-1 --force-new-deployment'
            }
         }
     }
 }

