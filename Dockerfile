FROM kalilinux/kali-rolling

# OS update
RUN echo "deb http://old.kali.org/kali sana main non-free contrib" >> ./etc/apt/sources.list
RUN apt-get update && apt-get -yu dist-upgrade -y

# Install tools
RUN apt-get -y install wget unzip

RUN apt-get install -y openjdk-11-jdk

# Install golang
RUN apt-get install -y golang-go

# Python ENV installation
RUN apt-get install -y \
  python2.7 \
  subversion \
  wget \
  whatweb \
  nmap \
  sslyze \
  nikto \
  theharvester \
  whois \
  dnsmap \
  sublist3r \
  knockpy \
  wig \
  dirb \
  golismero

RUN cd /tmp && \
    wget http://dl.bintray.com/groovy/maven/apache-groovy-binary-3.0.4.zip && \
    unzip apache-groovy-binary-3.0.4.zip && \
    mv groovy-3.0.4 /groovy && \
    rm apache-groovy-binary-3.0.4.zip

# Environment variable section

ENV NMAP_SCRIPTS_REPOSITORY_URL https://svn.nmap.org/nmap/scripts/

ENV GROOVY_HOME /groovy
ENV PATH $GROOVY_HOME/bin/:$PATH

ENV GOROOT=/usr/lib/go
ENV GOPATH=$HOME/go
ENV PATH=$GOPATH/bin:$GOROOT/bin:$PATH

# Debug Port
EXPOSE 5050

RUN mkdir -p /usr/src
RUN mkdir -p /usr/app

# Tool and script installations
# nmap dependencies
RUN svn checkout  https://svn.nmap.org/nmap/scripts/ nse

# assetfinder installation
RUN go get -u github.com/tomnomnom/assetfinder
RUN go get -u github.com/tomnomnom/httprobe

WORKDIR /usr/app

# add jar and default config
COPY ./target/groovy-gadgeto-scanner-full.jar /usr/app/
COPY ./config /usr/app/config

VOLUME /usr/src

#ENTRYPOINT ["/usr/bin/java","-jar","/usr/app/groovy-gadgeto-scanner-full.jar", "--config", "/usr/app/config"]