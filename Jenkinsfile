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
                    sh 'ls service'
                }

            }
        }

        stage('build') {
            agent none
            steps {
                container('maven') {
                    sh 'mvn clean package -Dmaven.test.skip=true'
                    dir('hospital-manage'){
                        sh 'ls'
                        sh 'mvn clean package -Dmaven.test.skip=true'
                    }
                    sh 'ls service'
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
                            sh 'ls gateway'
                            sh 'docker build -t gateway:latest -f gateway/Dockerfile  ./gateway/'
                        }

                    }
                }

                stage('构建service-cmn镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'ls service/service_cmn'
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

        stage('pull_image') {
            parallel {
                stage('推送hospital-manage镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'docker logout'
                            sh 'docker login'
                            withCredentials([usernamePassword(credentialsId : 'aliyun-docker-registry' ,usernameVariable : 'DOCKER_USER_VAR' ,passwordVariable : 'DOCKER_PWD_VAR' ,)]) {
                                sh 'echo "$DOCKER_PWD_VAR" | docker login $REGISTRY -u "$DOCKER_USER_VAR" --password-stdin'
                                sh 'docker tag hospital-manage:latest $REGISTRY/$DOCKERHUB_NAMESPACE/hospital-manage:SNAPSHOT-$BUILD_NUMBER'
                                sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/hospital-manage:SNAPSHOT-$BUILD_NUMBER'
                            }

                        }

                    }
                }

                stage('推送server-gateway镜像') {
                    agent none
                    steps {
                        container('maven') {
                            withCredentials([usernamePassword(credentialsId : 'aliyun-docker-registry' ,usernameVariable : 'DOCKER_USER_VAR' ,passwordVariable : 'DOCKER_PWD_VAR' ,)]) {
                                sh 'echo "$DOCKER_PWD_VAR" | docker login $REGISTRY -u "$DOCKER_USER_VAR" --password-stdin'
                                sh 'docker tag server-gateway:latest $REGISTRY/$DOCKERHUB_NAMESPACE/server-gateway:SNAPSHOT-$BUILD_NUMBER'
                                sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/server-gateway:SNAPSHOT-$BUILD_NUMBER'
                            }

                        }

                    }
                }

                stage('推送service-cmn镜像') {
                    agent none
                    steps {
                        container('maven') {
                            withCredentials([usernamePassword(credentialsId : 'aliyun-docker-registry' ,usernameVariable : 'DOCKER_USER_VAR' ,passwordVariable : 'DOCKER_PWD_VAR' ,)]) {
                                sh 'echo "$DOCKER_PWD_VAR" | docker login $REGISTRY -u "$DOCKER_USER_VAR" --password-stdin'
                                sh 'docker tag service-cmn:latest $REGISTRY/$DOCKERHUB_NAMESPACE/service-cmn:SNAPSHOT-$BUILD_NUMBER'
                                sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/service-cmn:SNAPSHOT-$BUILD_NUMBER'
                            }

                        }

                    }
                }

                stage('推送service-hosp镜像') {
                    agent none
                    steps {
                        container('maven') {
                            withCredentials([usernamePassword(credentialsId : 'aliyun-docker-registry' ,usernameVariable : 'DOCKER_USER_VAR' ,passwordVariable : 'DOCKER_PWD_VAR' ,)]) {
                                sh 'echo "$DOCKER_PWD_VAR" | docker login $REGISTRY -u "$DOCKER_USER_VAR" --password-stdin'
                                sh 'docker tag service-hosp:latest $REGISTRY/$DOCKERHUB_NAMESPACE/service-hosp:SNAPSHOT-$BUILD_NUMBER'
                                sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/service-hosp:SNAPSHOT-$BUILD_NUMBER'
                            }

                        }

                    }
                }

                stage('推送service-order镜像') {
                    agent none
                    steps {
                        container('maven') {
                            withCredentials([usernamePassword(credentialsId : 'aliyun-docker-registry' ,usernameVariable : 'DOCKER_USER_VAR' ,passwordVariable : 'DOCKER_PWD_VAR' ,)]) {
                                sh 'echo "$DOCKER_PWD_VAR" | docker login $REGISTRY -u "$DOCKER_USER_VAR" --password-stdin'
                                sh 'docker tag service-order:latest $REGISTRY/$DOCKERHUB_NAMESPACE/service-order:SNAPSHOT-$BUILD_NUMBER'
                                sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/service-order:SNAPSHOT-$BUILD_NUMBER'
                            }

                        }

                    }
                }

                stage('推送service-oss镜像') {
                    agent none
                    steps {
                        container('maven') {
                            withCredentials([usernamePassword(credentialsId : 'aliyun-docker-registry' ,usernameVariable : 'DOCKER_USER_VAR' ,passwordVariable : 'DOCKER_PWD_VAR' ,)]) {
                                sh 'echo "$DOCKER_PWD_VAR" | docker login $REGISTRY -u "$DOCKER_USER_VAR" --password-stdin'
                                sh 'docker tag service-oss:latest $REGISTRY/$DOCKERHUB_NAMESPACE/service-oss:SNAPSHOT-$BUILD_NUMBER'
                                sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/service-oss:SNAPSHOT-$BUILD_NUMBER'
                            }

                        }

                    }
                }

                stage('推送service-sms镜像') {
                    agent none
                    steps {
                        container('maven') {
                            withCredentials([usernamePassword(credentialsId : 'aliyun-docker-registry' ,usernameVariable : 'DOCKER_USER_VAR' ,passwordVariable : 'DOCKER_PWD_VAR' ,)]) {
                                sh 'echo "$DOCKER_PWD_VAR" | docker login $REGISTRY -u "$DOCKER_USER_VAR" --password-stdin'
                                sh 'docker tag service-sms:latest $REGISTRY/$DOCKERHUB_NAMESPACE/service-sms:SNAPSHOT-$BUILD_NUMBER'
                                sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/service-sms:SNAPSHOT-$BUILD_NUMBER'
                            }

                        }

                    }
                }

                stage('推送service-statistics镜像') {
                    agent none
                    steps {
                        container('maven') {
                            withCredentials([usernamePassword(credentialsId : 'aliyun-docker-registry' ,usernameVariable : 'DOCKER_USER_VAR' ,passwordVariable : 'DOCKER_PWD_VAR' ,)]) {
                                sh 'echo "$DOCKER_PWD_VAR" | docker login $REGISTRY -u "$DOCKER_USER_VAR" --password-stdin'
                                sh 'docker tag service-statistics:latest $REGISTRY/$DOCKERHUB_NAMESPACE/service-statistics:SNAPSHOT-$BUILD_NUMBER'
                                sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/service-statistics:SNAPSHOT-$BUILD_NUMBER'
                            }

                        }

                    }
                }

                stage('推送service-task镜像') {
                    agent none
                    steps {
                        container('maven') {
                            withCredentials([usernamePassword(credentialsId : 'aliyun-docker-registry' ,usernameVariable : 'DOCKER_USER_VAR' ,passwordVariable : 'DOCKER_PWD_VAR' ,)]) {
                                sh 'echo "$DOCKER_PWD_VAR" | docker login $REGISTRY -u "$DOCKER_USER_VAR" --password-stdin'
                                sh 'docker tag service-task:latest $REGISTRY/$DOCKERHUB_NAMESPACE/service-task:SNAPSHOT-$BUILD_NUMBER'
                                sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/service-task:SNAPSHOT-$BUILD_NUMBER'
                            }

                        }

                    }
                }

                stage('推送service-user镜像') {
                    agent none
                    steps {
                        container('maven') {
                            withCredentials([usernamePassword(credentialsId : 'aliyun-docker-registry' ,usernameVariable : 'DOCKER_USER_VAR' ,passwordVariable : 'DOCKER_PWD_VAR' ,)]) {
                                sh 'echo "$DOCKER_PWD_VAR" | docker login $REGISTRY -u "$DOCKER_USER_VAR" --password-stdin'
                                sh 'docker tag service-user:latest $REGISTRY/$DOCKERHUB_NAMESPACE/service-user:SNAPSHOT-$BUILD_NUMBER'
                                sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/service-user:SNAPSHOT-$BUILD_NUMBER'
                            }

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