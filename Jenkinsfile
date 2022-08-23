pipeline {
     agent any
     stages {
         stage('Prepare') {
             steps {
                 script {
                     if (env.BRANCH_NAME == 'master') {
                         sh "aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin ${env.PROD_ECR}"
                     } else {
                         sh "aws ecr-public get-login-password --region us-east-1 | docker login --username AWS --password-stdin ${env.DEV_ECR}"
                     }
                 }
             }
         }

         stage('Gradle Build') {
             steps {
                 sh "./gradlew clean build"
             }
         }

         stage('Docker Build') {
             steps {
                 script {
                    if (env.BRANCH_NAME == 'master') {
                        sh "docker build -t connectable ."
                    } else {
                        sh "docker build -t dev-connectable ."
                    }
                 }
             }
         }

         stage('Aws ECR Upload') {
             steps {
                 script {
                     if (env.BRANCH_NAME == 'master') {
                         sh "docker tag connectable:latest ${env.PROD_ECR}:latest"
                         sh "docker push ${env.PROD_ECR}:latest"
                     } else {
                         sh "docker tag dev-connectable:latest ${env.DEV_ECR}:latest"
                         sh "docker push ${env.DEV_ECR}:latest"
                     }
                 }
             }
         }

         stage('Aws ECS Deploy') {
             steps {
                 script {
                     if (env.BRANCH_NAME == 'master') {
                        sh "aws ecs update-service --region ap-northeast-2 --cluster ${env.PROD_ECS_CLUSTER} --service ${env.PROD_ECS_SERVICE} --force-new-deployment"
                     } else {
                        sh "aws ecs update-service --region ap-northeast-2 --cluster ${env.DEV_ECS_CLUSTER} --service ${env.DEV_ECS_SERVICE} --force-new-deployment"
                     }
                 }
             }
         }
     }
}
