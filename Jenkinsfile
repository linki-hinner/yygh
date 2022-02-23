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
                    sh 'git clone --branch $BRANCH_NAME $GIT_URL'
                    sh 'ls yygh'
                }

            }
        }

        stage('deploy-build'){
            parallel{
                stage("hospital-manage"){
                    agent none
                    steps{
                        sh "chmod 700 yygh/hospital-manage/deploy"
                        sh "cd yygh/hospital-manage/deploy && sed s/@URL/${GIT_URL_2}/g deploy.yml > dep_tmp"
                        sh "cd yygh/hospital-manage/deploy && sed -i s/@BUILD_NUMBER/${BUILD_NUMBER}/g dep_tmp > deploy.yml"
                    }
                }
            }
        }

        stage('build') {
            parallel {
                stage('maven主体') {
                    agent none
                    steps {
                        container('maven') {
                            dir('yygh') {
                                sh 'ls'
                                sh 'pwd'
                                sh 'mvn clean package -Dmaven.test.skip=true'
                            }

                        }

                    }
                }

                stage('maven模拟') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'cd yygh/hospital-manage && ls && pwd && mvn clean package'
                        }

                    }
                }
            }
        }



        stage('docker_build') {
            parallel {
                stage('构建hospital-manage镜像') {
                    agent none
                    steps {
                        container('maven') {
                            dir('yygh') {
                                sh 'ls hospital-manage'
                                sh 'docker build -t hospital-manage:latest -f hospital-manage/Dockerfile  ./hospital-manage/'
                            }

                        }

                    }
                }

                stage('构建gateway镜像') {
                    agent none
                    steps {
                        container('maven') {
                            dir('yygh') {
                                sh 'ls gateway'
                                sh 'docker build -t gateway:latest -f gateway/Dockerfile  ./gateway/'
                            }

                        }

                    }
                }

                stage('构建service-cmn镜像') {
                    agent none
                    steps {
                        container('maven') {
                            dir('yygh') {
                                sh 'ls service/service-cmn'
                                sh 'docker build -t service-cmn:latest -f service/service-cmn/Dockerfile  ./service/service-cmn/'
                            }

                        }

                    }
                }

                stage('构建service-hosp镜像') {
                    agent none
                    steps {
                        container('maven') {
                            dir('yygh') {
                                sh 'ls service/service-hosp'
                                sh 'docker build -t service-hosp:latest -f service/service-hosp/Dockerfile  ./service/service-hosp/'
                            }

                        }

                    }
                }

                stage('构建service-order镜像') {
                    agent none
                    steps {
                        container('maven') {
                            dir('yygh') {
                                sh 'ls service/service-order'
                                sh 'docker build -t service-order:latest -f service/service-order/Dockerfile  ./service/service-order/'
                            }

                        }

                    }
                }

                stage('构建service-oss镜像') {
                    agent none
                    steps {
                        container('maven') {
                            dir('yygh') {
                                sh 'ls service/service-oss'
                                sh 'docker build -t service-oss:latest -f service/service-oss/Dockerfile  ./service/service-oss/'
                            }

                        }

                    }
                }

                stage('构建service-msm镜像') {
                    agent none
                    steps {
                        container('maven') {
                            dir('yygh') {
                                sh 'ls service/service-msm'
                                sh 'docker build -t service-msm:latest -f service/service-msm/Dockerfile  ./service/service-msm/'
                            }

                        }

                    }
                }

                stage('构建service-user镜像') {
                    agent none
                    steps {
                        container('maven') {
                            dir('yygh') {
                                sh 'ls service/service-user'
                                sh 'docker build -t service-user:latest -f service/service-user/Dockerfile  ./service/service-user/'
                            }

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
                            sh 'docker login -u $DOCKER_USER -p $DOCKER_PASSWORD'
                            sh 'docker tag hospital-manage:latest wocaoeee/yygh:hospital-manageSNAPSHOT-$BUILD_NUMBER'
                            sh 'docker push  wocaoeee/yygh:hospital-manageSNAPSHOT-$BUILD_NUMBER'
                        }

                    }
                }

                stage('推送gateway镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh "docker login -u $DOCKER_USER -p $DOCKER_PASSWORD"
                            sh "docker tag gateway:latest wocaoeee/yygh:gatewaySNAPSHOT-$BUILD_NUMBER"
                            sh "docker push  wocaoeee/yygh:gatewaySNAPSHOT-$BUILD_NUMBER"
                        }

                    }
                }

                stage('推送service-cmn镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh "docker login -u $DOCKER_USER -p $DOCKER_PASSWORD"
                            sh "docker tag service-cmn:latest wocaoeee/yygh:service-cmnSNAPSHOT-$BUILD_NUMBER"
                            sh "docker push  wocaoeee/yygh:service-cmnSNAPSHOT-$BUILD_NUMBER"
                        }

                    }
                }

                stage('推送service-hosp镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'docker login -u $DOCKER_USER -p $DOCKER_PASSWORD'
                            sh 'docker tag service-hosp:latest wocaoeee/yygh:service-hospSNAPSHOT-$BUILD_NUMBER'
                            sh 'docker push  wocaoeee/yygh:service-hospSNAPSHOT-$BUILD_NUMBER'
                        }

                    }
                }

                stage('推送service-order镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'docker login -u $DOCKER_USER -p $DOCKER_PASSWORD'
                            sh 'docker tag service-order:latest wocaoeee/yygh:service-orderSNAPSHOT-$BUILD_NUMBER'
                            sh 'docker push  wocaoeee/yygh:service-orderSNAPSHOT-$BUILD_NUMBER'
                        }

                    }
                }

                stage('推送service-oss镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'docker login -u $DOCKER_USER -p $DOCKER_PASSWORD'
                            sh 'docker tag service-oss:latest wocaoeee/yygh:service-ossSNAPSHOT-$BUILD_NUMBER'
                            sh 'docker push  wocaoeee/yygh:service-ossSNAPSHOT-$BUILD_NUMBER'
                        }

                    }
                }

                stage('推送service-msm镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'docker login -u $DOCKER_USER -p $DOCKER_PASSWORD'
                            sh 'docker tag service-msm:latest wocaoeee/yygh:service-msmSNAPSHOT-$BUILD_NUMBER'
                            sh 'docker push  wocaoeee/yygh:service-msmSNAPSHOT-$BUILD_NUMBER'
                        }

                    }
                }

                stage('推送service-user镜像') {
                    agent none
                    steps {
                        container('maven') {
                            sh 'docker login -u $DOCKER_USER -p $DOCKER_PASSWORD'
                            sh 'docker tag service-user:latest wocaoeee/yygh:service-userSNAPSHOT-$BUILD_NUMBER'
                            sh 'docker push wocaoeee/yygh:service-userSNAPSHOT-$BUILD_NUMBER'
                        }

                    }
                }

            }
        }

        stage('deploy') {
            parallel {
                stage('hospital-manage') {
                    agent none
                    steps {
                        sh 'cat yygh/hospital-manage/deploy/deploy.yml'
                        kubernetesDeploy(enableConfigSubstitution: true, deleteResource: false, configs: 'yygh/hospital-manage/deploy/deploy.yml', kubeconfigId: "$KUBE_CONFIG_ID")
                    }
                }

            }
        }

    }
    environment {
        BRANCH_NAME = 'release'
        GIT_URL = 'git://github.com/linki-hinner/yygh.git'
        DOCKER_USER = 'wocaoeee'
        DOCKER_PASSWORD = 'Chl159951'
        KUBE_CONFIG_ID = 'yygh-kubeconfig'
    }
}