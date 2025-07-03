# Groovy Gadgeto Scanner

!!!!WORK IN PROGRESS!!!

Runs a set of security checks grouped by pentesting phase and configurable as pen testing profiles.
For each situation, the container and the profile can be equipped with the fitting gadget. 

Each check uses of a set of underlying tools such as nmap or sslyze.
The application uses a dockerized Kali environment which has the required tools preinstalled. 

# Why
Because it costs a lot of time to pre execute the automation part of each pen testing phase and the plethora of tools over and over again.
Additionally each environment may have different pre conditions and a slightly different stack. 
So it makes sense to prepare and maintain different tools setups and configurations.

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
    
    
## Create and use your own config

Clone the repository and build the container.
Prepare a config directory and put your own custom configs in e.g. in the current location

    mkdir customconfig
    touch example-info-phase.yaml
    
Open the yaml file and add a phase definition. The following one is named info and contains to nmap based checks

    id: info
    name: Information Phase
    description: "Information gathering phase"
    modules:
      - name: "nmap fast port checks"
        executable: "nmap"
        args: "-F --open -Pn "
        severity: 0
        positiveResponse:
          - "RE:22/tcp"
     - name: "nmap cors"
        executable: "nmap"
        args: "--script=/usr/app/nse/http-cors "
        severity: 0
        mode: print

Now start the contaner it while adding your own config directory volume. 
Use the info phase.
    
    docker run -p 5050:5050 --rm -it -v $(pwd)/customconfig:/usr/app/customconfig gadgetoscanner --phase info ---config /usr/app/customconfig --url <target>


# Development Guide

Build the tool
    
    mvn clean package
    
Build the docker image

    docker build --no-cache -t gadgetoscanner . 
        
## Debug the application inside the container

Run the application while developing    

    java -agentlib:jdwp=transport=dt_socket,address=5050,suspend=y,server=y -jar target/groovy-gadgeto-scanner-1.0-SNAPSHOT.jar --phase info --url http://172.17.0.3:8080/WebGoat
        
## Installation for development

Run a container for local development
    
    docker run -p 5050:5050 --rm -it -v $(pwd):/usr/app gadgetoscanner
    
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

## Python Recon Agent
A simple script to gather DNS information, HTTP headers and potential API endpoints from a target URL.

Usage:
```
python3 scripts/recon_agent.py --url https://example.com --output report.json
```
The script stores a JSON report summarizing subdomains, DNS records, headers and more.

## LLM Recon Orchestrator
An optional helper script uses a locally running [Ollama](https://ollama.com) model
to post-process and summarise the recon results.

Ensure the `ollama` daemon is running and the Python requirements are installed:

```bash
pip install -r scripts/requirements.txt
ollama serve &
```

Run the orchestrator to execute the recon agent and get a short summary:

```bash
python3 scripts/orchestrator.py https://example.com --model llama3
```

