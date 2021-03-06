/** 
	此Jenkins流水线完成如下流程:
	1, 接收git tag的push 事件
	2， maven打包
	3， 构建docker镜像
	4， 远程发布
	5， 通知企微机器人
*/
// 以下是需要修改的常量--------------------
def R_NAME = "jiu-shu/jvmdemo"  
def WEBHOOK_TOKEN = "jvmdemo"  // http://JENKINS_URL/generic-webhook-trigger/invoke?token=${WEBHOOK_TOKEN}
def S_NAME = 'JVM Utils Demo'
def serverHost = "121.42.12.209"
def registryServer = 'https://registry.cn-hangzhou.aliyuncs.com/'
def jenkinsCredentialId = 'registry-aliyun-credentials'
def sshCredentialId = 'wuhan_vm'
def robotUrl = 'https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=bf32cb1d-2fc9-410d-a28f-7427b4aa0ffb'


// ************ 下面的配置可以保持不变 *************** //
//变量已经方法定义
def remote = [:]
remote.name = 'Server'
remote.host = serverHost
remote.allowAnyHosts = true
//构建流程定义
pipeline {
    agent none //后续步骤的执行环境 这个位置是全局的执行环境，如果配置了，后续步骤都是这个执行环境，该文件中不同步骤执行环境不同，所以此处配置为none
    triggers { // Generic Webhook Trigger 配置， 第一次需要手动触发让配置保存至jenkins任务
        GenericTrigger(
            genericVariables: [//从webhook的请求中获取相应的参数
              [key: 'ref', value: '$.ref'],
              [key: 'tag_name', value: '$.ref']
            ],
            token: WEBHOOK_TOKEN ,//webhook的触发token，不同的分支可以设置不同的token进行区分
            causeString: ' Triggered on ref' ,
            printContributedVariables: true,
            printPostContent: true//打印webhook的请求参数
        )
    }
    stages {
        stage('package') { //stage定义一个构建阶段
            agent {//配置一个maven的环境
                docker {
                    image 'maven:3-alpine'
                    args '-v /root/.m2:/root/.m2'
                }
            }
            steps {
                script {
                	sh 'mvn clean package'
                }
            }
        }
        stage('Build  Image') {
        	when { tag "refs/tags/release-*" }
        	agent any
            steps {
                script {
                        env.tagv = ref.substring(18)
                        docker.withRegistry(registryServer,jenkinsCredentialId) {//登录到远程docker仓库，第二个参数是在Jenkins中配置的凭据，详情见官网
                            def image = docker.build(R_NAME)
                            image.push("latest")
                            image.push(env.tagv)
                            sh "echo docker image  push success"
                        }
                    }
            }
        }
        stage('deploy') {
        	when { tag "refs/tags/release-*" }
        	agent any
            steps {
                script {
                		//ssh到远程机器上执行一段自身机器上的脚本
                        withCredentials([usernamePassword(credentialsId: sshCredentialId, passwordVariable: 'password', usernameVariable: 'username')]) {
                            remote.user = userName
                            remote.password = password
                            sshScript remote: remote, script: './jenkins/deploy.sh'//dev_deploy脚本是在Jenkins机器上
                        }
                }
            }
        }
        stage('Notification'){ // 通知企微机器人
        	when { tag "refs/tags/release-*" }
        	agent any
            steps {
                script{
                    def requestBody = """
                        {"msgtype": "markdown","markdown": {"content": "## ${S_NAME} ${env.tagv} is released"  }}
                    """
                    def response = httpRequest acceptType: 'APPLICATION_JSON', contentType: 'APPLICATION_JSON', httpMode: 'POST', requestBody: requestBody, url: "${robotUrl}"
                    sh "echo $response.content"
                }
            }
        }
        
      }
    }
