pipeline {
    agent {
        node {
            label 'maven'
        }

    }
    stages {
        stage('git-clone') {
            agent none
            steps {
                container('maven') {
                    sh 'ls'
                    git(url: 'https://github.com/linki-hinner/yygh.git', branch: 'release', changelog: true, poll: false)
                    sh 'ls'
                }

            }
        }

        stage('build') {
            agent none
            steps {
                container('maven') {
                    sh 'ls'
                    sh 'mvn clean package -Dmaven.test.skip=true'
                    sh 'ls'
                }

            }
        }

        stage('docker_build') {
            parallel {
                stage('构建hospital-manage镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'ls hospital-manage'
                            sh 'docker build -t hospital-manage:latest -f hospital-manage/Dockerfile  ./hospital-manage/'
                        }

                    }
                }

                stage('构建server-gateway镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'ls server-gateway'
                            sh 'docker build -t server-gateway:latest -f server-gateway/Dockerfile  ./server-gateway/'
                        }

                    }
                }

                stage('构建service-cmn镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'ls service/service-cmn'
                            sh 'docker build -t service-cmn:latest -f service/service-cmn/Dockerfile  ./service/service-cmn/'
                        }

                    }
                }

                stage('构建service-hosp镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'ls service/service-hosp'
                            sh 'docker build -t service-hosp:latest -f service/service-hosp/Dockerfile  ./service/service-hosp/'
                        }

                    }
                }

                stage('构建service-order镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'ls service/service-order'
                            sh 'docker build -t service-order:latest -f service/service-order/Dockerfile  ./service/service-order/'
                        }

                    }
                }

                stage('构建service-oss镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'ls service/service-oss'
                            sh 'docker build -t service-oss:latest -f service/service-oss/Dockerfile  ./service/service-oss/'
                        }

                    }
                }

                stage('构建service-sms镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'ls service/service-sms'
                            sh 'docker build -t service-sms:latest -f service/service-sms/Dockerfile  ./service/service-sms/'
                        }

                    }
                }

                stage('构建service-user镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'ls service/service-user'
                            sh 'docker build -t service-user:latest -f service/service-user/Dockerfile  ./service/service-user/'
                        }

                    }
                }

            }
        }

    }

    environment {
        DOCKER_CREDENTIAL_ID = 'dockerhub-id'
        GITHUB_CREDENTIAL_ID = 'github-id'
        KUBECONFIG_CREDENTIAL_ID = 'demo-kubeconfig'
        REGISTRY = 'docker.io'
        DOCKERHUB_NAMESPACE = 'docker_username'
        GITHUB_ACCOUNT = 'kubesphere'
        APP_NAME = 'devops-java-sample'
    }
}