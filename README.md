# Groovy Gadgeto Scanner

Runs a set of security checks grouped by pentesting phase and configurable as pen testing profiles.
For each situation, the container and the profile can be equipped with the fitting gadget. 

Each check uses of a set of underlying tools such as nmap or sslyze.
The application uses a Dockerized Kali environment which has those tools preinstalled. 

# Run it

Dockerized

    docker run -p 5050:5050 --rm -it gadgetoscanner --phase <phasename> --url <target>
    
Example    
    
    docker run -p 5050:5050 --rm -it gadgetoscanner --phase info --url localhost:8080

Without docker on a local instance with all dependencies and tools available

    java -jar groovy-gadgeto-scanner.jar --phase <phasename> --url <target> 

## Parameters

The tool can either show all available phases through the list parameter or execute a scan
List execution:
--list Show all available phases (mandatory)

Scan execution:

--phase <phasename> (mandatory)
--url <target> (mandatory)
--config <path to configlocation>

# Development Guide

Build the tool
    
    mvn clean package
    
Build the docker image

    docker build --no-cache -t gadgetoscanner . 
        
## Debug the application inside the container

Run the application while developing    

    java -agentlib:jdwp=transport=dt_socket,address=5050,suspend=y,server=y -jar target/groovy-gadgeto-scanner-1.0-SNAPSHOT.jar --phase info --url http://172.17.0.3:8080/WebGoat
        
## Installation and development workflow

Run a container for local development
    
    docker run -p 5050:5050 --rm -it -v $(pwd):/usr/src gadgetoscanner
    
Build the scanner, outside of the container

    mvn clean package    
    
Run the scanner freshly build inside the container 
    
    java -jar /usr/src/target/groovy-gadgeto-scanner-full.jar --config /usr/app/customconfig --phase whatever --url http://172.17.0.3:8080/WebGoat
    
Debug it
    
    java -agentlib:jdwp=transport=dt_socket,address=5050,suspend=y,server=y -jar /usr/src/target/groovy-gadgeto-scanner-full.jar --phase info --url http://172.17.0.3:8080/WebGoat
    
## Testing the application

WebGoat can be used to test the application. https://owasp.org/www-project-webgoat/

    docker pull webgoat/webgoat-8.0
    docker run -p 8080:8080 -t webgoat/webgoat-8.0   
    
The application is accessible via the following URL

    http://localhost:8080/WebGoat/login 
    
### Troubleshoot during testing    
    
Check docker networking to find internal IPs

     docker network inspect bridge    
    
Then connect with your remote debugger on port 50505
    
## Development Backlog

* Severity ranking and vulnerability description is part of the results
* Installation check for tools (done)
* More default phases and checks
* Interactive mode
* Write to report directory  
* Easy simple start script
* Debug support for easy simple start script 
* List executable dependencies for different phases
* Read phases from config files in config directory (done)
* Config phases reference module.yaml (done)
* Check if dependencies are available 
* Report generation     
* Load configs from remote resources