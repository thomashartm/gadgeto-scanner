FROM kalilinux/kali-rolling

# OS update
RUN apt-get update && apt-get -yu dist-upgrade -y

# Install tools
RUN apt-get -y install wget unzip

RUN apt-get install -y openjdk-11-jdk

# Python ENV installation
RUN apt-get install -y \
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
  wig

RUN cd /tmp && \
    wget http://dl.bintray.com/groovy/maven/apache-groovy-binary-3.0.4.zip && \
    unzip apache-groovy-binary-3.0.4.zip && \
    mv groovy-3.0.4 /groovy && \
    rm apache-groovy-binary-3.0.4.zip

ENV NMAP_SCRIPTS_REPOSITORY_URL https://svn.nmap.org/nmap/scripts/

#ENV JAVA_HOME /jdk
ENV GROOVY_HOME /groovy
ENV PATH $GROOVY_HOME/bin/:$PATH

# Debug Port
EXPOSE 5050

RUN mkdir -p /usr/src
RUN mkdir -p /usr/app
RUN svn checkout  https://svn.nmap.org/nmap/scripts/ nse
#RUN wget -r -nH --cut-dirs=2 --no-parent --reject="index.html*" https://svn.nmap.org/nmap/scripts/http-backup-finder.nse -P /usr/app/nse

WORKDIR /usr/app

# add jar and default config
COPY ./target/groovy-gadgeto-scanner-full.jar /usr/app/
COPY ./config /usr/app/config

VOLUME /usr/src

ENTRYPOINT ["/usr/bin/java","-jar","/usr/app/groovy-gadgeto-scanner-full.jar", "--config", "/usr/app/config"]