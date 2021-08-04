pipeline {
    agent any

     environment{
         scannerHome = tool name:'SonarQubeScanner'
       dockerhubUsername = 'himanshiverma05'
	  
		
		username='himanshiverma'
		project_id = 'testjenkinsproject'
	    cluster_name = 'testjenkinscluster'
	    location = 'us-central1-c'
	    credentials_id = 'testJenkinsGCP'
		
    }
    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "Maven3"
		jdk "JDK11"
    }
    
triggers {
      pollSCM('H/2 * * * *') 
    }         
    
    options{
        timestamps()
        timeout(time: 1, unit: 'HOURS')
        skipDefaultCheckout()
        buildDiscarder(logRotator(
            numToKeepStr: '3',
            daysToKeepStr: '30'
            ))
    }
    
    stages {
	stage('gitCheckout') {
                steps {
				echo 'Checkout code'
                    git branch: "${BRANCH_NAME}",credentialsId: 'github', url:'https://github.com/HimanshiVerma05/app_himanshiverma.git'
                }
            }
      /*  stage('gitCheckout'){
             steps {
                
               echo 'Checkout code'
                git poll:true,credentialsId: 'github',url:'https://github.com/HimanshiVerma05/AzureBackend.git'
              
             }
        } */
  
		
		stage('Code Build'){
            steps{
                echo 'doing maven build '
                bat 'mvn clean package'
            }
        }
		
		 stage('Sonar Analysis') {
		 
		 
                when {
                    branch 'master'
                }
                steps {
				echo 'Sonar Analysis.'
                 
				 withSonarQubeEnv('Test_Sonar') {
					bat "${scannerHome}/bin/sonar-scanner \
					-Dsonar.projectKey=sonar-himanshiverma \
					-Dsonar.projectName=sonar-himanshiverma \
					-Dsonar.host.url=http://localhost:9000 \
					-Dsonar.java.binaries=target/classes"
                }
                }
            }
		
		 
        
        stage('Unit Testing'){
		 when {
                    branch 'develop'
                }
            steps{
                echo 'doing unit testing'
                bat 'mvn test'
            }
        }
		
		stage('Docker Image build') {
             steps{
                 echo "Docker Image step "
               
                 bat "docker build -t i-${username}-${BRANCH_NAME}:${BUILD_NUMBER} --no-cache -f Dockerfile ."
				 
				 
             }
         }
		
		stage('Containers') {
                parallel {
                    stage('PreContainerCheck') {
                        steps {
                           
						   script{
						      try{
							  bat "docker stop c-${username}-${BRANCH_NAME}"
							  bat "docker container rm c-${username}-${BRANCH_NAME}"
							  }
							  catch(Exception ex){
							  }
						   }
                        }                        
                    }
                    stage('PushtoDockerHub') {
                        steps {
						   
							    
                               bat "docker tag i-${username}-${BRANCH_NAME}:${BUILD_NUMBER} ${dockerhubUsername}/i-${username}-${BRANCH_NAME}:${BUILD_NUMBER}"
							   
               
                 
               
                 withDockerRegistry([credentialsId:'DockerHub' , url:""]){
                
                                bat "docker push ${dockerhubUsername}/i-${username}-${BRANCH_NAME}:${BUILD_NUMBER}"
							  
                
                 }
                        }                        
                    }
                }
            }
	
		 stage('Docker Deployment') {
                
				
				
                   
                        steps { 


							script{
							def portNumber
							 if(BRANCH_NAME == 'master'){
									portNumber = 7200
								}
							if(BRANCH_NAME == 'develop'){
									portNumber = 7300
								}
								
							
								
								bat "docker run --name c-${username}-${BRANCH_NAME} -d -p ${portNumber}:8100 ${dockerhubUsername}/i-${username}-${BRANCH_NAME}:${BUILD_NUMBER}"
								
								
							}												
                                                       
                            
                        }                    
                    
                
				
				
				
            }
  
         
stage('Kubernetes Deployment on local ') {

                steps{
                  script{
							
							 if(BRANCH_NAME == 'master'){
									powershell "(Get-Content ${WORKSPACE}\\deployment.yaml).Replace('{{USERNAME}}', '${username}').Replace('{{BRANCH_NAME}}', '${BRANCH_NAME}').Replace('{{BUILD_NUMBER}}', '${BUILD_NUMBER}').Replace('{{PORT}}', '30157') | Out-File ${WORKSPACE}\\deployment.yaml"
		
		
							 bat "kubectl config use-context docker-desktop"
                            bat "kubectl apply -f ${WORKSPACE}\\deployment.yaml"
								}
								
								
							if(BRANCH_NAME == 'develop'){
									 powershell "(Get-Content ${WORKSPACE}\\deployment.yaml).Replace('{{USERNAME}}', '${username}').Replace('{{BRANCH_NAME}}', '${BRANCH_NAME}').Replace('{{BUILD_NUMBER}}', '${BUILD_NUMBER}').Replace('{{PORT}}', '30158') | Out-File ${WORKSPACE}\\deployment.yaml"
		
		
		                    bat "kubectl config view"
                            bat "kubectl config use-context docker-desktop"
							
							bat "kubectl apply -f ${WORKSPACE}\\deployment.yaml"
							
					  //  step ([$class: 'KubernetesEngineBuilder', projectId: env.project_id, clusterName: env.cluster_name, location: env.location, manifestPattern: 'deployment.yaml', credentialsId: env.credentials_id, verifyDeployments: true])
								}
						}	
					}						




            }
			
			
			stage('kubernetes deployment on GKE'){
			 steps{
			  bat "kubectl config use-context gke_${project_id}_${location}_${cluster_name}"
			  bat "kubectl apply -f ${WORKSPACE}\\deployment.yaml"
			 }
			
			}

		 


    }
}


