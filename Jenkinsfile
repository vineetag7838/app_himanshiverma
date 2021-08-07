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
        
        maven "Maven3"
		//I have used jenkins jdk11 global tool configuration pointing to the jdk 11 on my machine because there was some issue with my client projects.
		// This step can be removed if you have jdk 11 installed on your machine and all path set 
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
	stage('Build') {
                steps {
				echo 'Checking out the code from GITHUB repo HimanshiVerma05/app_himanshiverma '
                    git branch: "${BRANCH_NAME}",credentialsId: 'github', url:'https://github.com/HimanshiVerma05/app_himanshiverma.git'
					
					echo 'Maven clean and build the code.'
                echo 'doing maven build '
                bat 'mvn clean package'
                }
            }
   
 		
		 stage('Sonar Analysis') {
		 
		 
                when {
                    branch 'master'
                }
                steps {
				echo 'Performing sonarqube analysis of the code.'
                 
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
                echo 'Performing unit testing .'
                bat 'mvn test'
            }
        }
		
		stage('Docker Image') {
             steps{
                 echo "Building the docker image "
               
                 bat "docker build -t i-${username}-${BRANCH_NAME}:${BUILD_NUMBER} --no-cache -f Dockerfile ."
				 
				 
             }
         }
		
		stage('Containers') {
                parallel {
                    stage('PreContainerCheck') {
                        steps {
                           
						   script{
						      try{
							  echo "Stop the container running on port "
							  bat "docker stop c-${username}-${BRANCH_NAME}"
							  echo "Removing the container running on port in order to vacate the port. "
							  bat "docker container rm c-${username}-${BRANCH_NAME}"
							  }
							  catch(Exception ex){
							  //for first time running the image it will throw exception because there will be no such container to be deleted.
							  }
						   }
                        }                        
                    }
                    stage('PushtoDockerHub') {
                        steps {
						   
							   echo "Pushing the image built to docker hub  "
                               bat "docker tag i-${username}-${BRANCH_NAME}:${BUILD_NUMBER} ${dockerhubUsername}/i-${username}-${BRANCH_NAME}:${BUILD_NUMBER}"
							               
                 withDockerRegistry([credentialsId:'DockerHub' , url:""]){
                
                                bat "docker push ${dockerhubUsername}/i-${username}-${BRANCH_NAME}:${BUILD_NUMBER}"
							  
                
                 }
                        }                        
                    }
                }
            }
	
		 stage('Docker deployment') {
         	        
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
  
         
stage('Kubernetes Deployment') {

                steps{
                  script{
							
							 if(BRANCH_NAME == 'master'){
							 
							 echo "Using single deployment file for both branches by replacing the variable content in the deployment file accordingly"
							 
									powershell "(Get-Content ${WORKSPACE}\\deployment.yaml).Replace('{{DOCKERHUBUSERNAME}}','${dockerhubUsername}').Replace('{{USERNAME}}', '${username}').Replace('{{BRANCH_NAME}}', '${BRANCH_NAME}').Replace('{{BUILD_NUMBER}}', '${BUILD_NUMBER}').Replace('{{PORT}}', '30157') | Out-File ${WORKSPACE}\\deployment.yaml"
		
		
							 echo "setting context for gke in branch master"
							 bat "kubectl config use-context gke_${project_id}_${location}_${cluster_name}"
                            bat "kubectl apply -f ${WORKSPACE}\\deployment.yaml"
							
							//setting context to docker-desktop in order to run on localhost
							 bat "kubectl config use-context docker-desktop"
                            bat "kubectl apply -f ${WORKSPACE}\\deployment.yaml"
							
							echo "firewall for port 30157"
							
							 try{
                        bat "gcloud compute firewall-rules create firewallformaster --allow tcp:30157"
                    }catch(Exception e){
                        //the try code will throw exception after the firewall already occurs hence handling.
                    }
								}
								
								
							if(BRANCH_NAME == 'develop'){
									 powershell "(Get-Content ${WORKSPACE}\\deployment.yaml).Replace('{{DOCKERHUBUSERNAME}}','${dockerhubUsername}').Replace('{{USERNAME}}', '${username}').Replace('{{BRANCH_NAME}}', '${BRANCH_NAME}').Replace('{{BUILD_NUMBER}}', '${BUILD_NUMBER}').Replace('{{PORT}}', '30158') | Out-File ${WORKSPACE}\\deployment.yaml"
		
		
		                   // bat "kubectl config view"                            
							//bat "kubectl config use-context gke_${project_id}_${location}_${cluster_name}"							
							//bat "gcloud config list --format=json"
							//bat "gcloud config list --format=json --configuration=default"
							 echo "setting context for gke in branch develop"					
							bat "kubectl config use-context gke_${project_id}_${location}_${cluster_name}"
							bat "kubectl apply -f ${WORKSPACE}\\deployment.yaml"
							
							//setting context to docker-desktop in order to run on localhost
							 bat "kubectl config use-context docker-desktop"
                            bat "kubectl apply -f ${WORKSPACE}\\deployment.yaml"
							
					 
					  echo "firewall for port 30158"
					  
					  try{
                        bat "gcloud compute firewall-rules create firewallfordevelop --allow tcp:30158"
                    }catch(Exception e){
                        //the try code will throw exception after the firewall already occurs hence handling.
                    }
					  
								}
								
								
								
						}	
					}						




            }
			
			
			

		 


    }
}


