# Groovy Gadjeto Scanner

Runs a set of checks grouped by pen testing phase.
Each check consists of a set of underlying tools such 
as nmap or sslyze.

The application is supposed to run in an environment which has those tools preinstalled. 
Therefore run it in the context of a Dockerized Kali environment.

# Development Guide

Build it
    
    mvn clean package

Run it (requires the tool dependencies to be installed)

    java -jar groovy-gadjeto-scanner.jar --phase <phasename> --url <target>
    
## Installation

Build the docker image

    docker build --no-cache -t gadjetoscanner .  
    
Run a container for local development
    
    docker run -p 50505:50505 --rm -it -v $(pwd):/usr/src gadjetoscanner
    
### Testing the application

WebGoat can be used to test the application. https://owasp.org/www-project-webgoat/

    docker pull webgoat/webgoat-8.0
    docker run -p 8080:8080 -t webgoat/webgoat-8.0   
    
The application is accessible via the following URL

    http://localhost:8080/WebGoat/login 
    
### Troubleshoot during testing    
    
Check docker networking to find internal IPs

     docker network inspect bridge    
    

    
## Run the application inside the container


Run the application while developing    

    java -agentlib:jdwp=transport=dt_socket,address=50505,suspend=y,server=y -jar target/groovy-gadjeto-scanner-1.0-SNAPSHOT.jar --phase info --url http://172.17.0.3:8080/WebGoat
    
Then connect with your remote debugger on port 50505
    
# Development Backlog

* Easy simple start script
* Debug support for easy simple start script 
* List executable dependencies for different phases
* Read phases from config files in config directory
* Config phases reference module.yaml
* Check if dependencies are available
* Report generation     
* Load configs from remote resources