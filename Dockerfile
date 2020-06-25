FROM kalilinux/kali-rolling

# OS update
RUN apt-get update && apt-get -yu dist-upgrade -y

# Install tools
RUN apt-get -y install wget unzip

RUN apt-get install -y openjdk-11-jdk

# Python ENV installation
RUN apt-get install -y \
  wget \
  nmap \
  sslyze \
  nikto \
  theharvester \
  whois \
  dnsmap

RUN cd /tmp && \
    wget http://dl.bintray.com/groovy/maven/apache-groovy-binary-3.0.4.zip && \
    unzip apache-groovy-binary-3.0.4.zip && \
    mv groovy-3.0.4 /groovy && \
    rm apache-groovy-binary-3.0.4.zip

#ENV JAVA_HOME /jdk
ENV GROOVY_HOME /groovy
ENV PATH $GROOVY_HOME/bin/:$PATH

# Debug Port
EXPOSE 5050

RUN mkdir -p /usr/src
RUN mkdir -p /usr/app
WORKDIR /usr/app

# add jar and default config
COPY ./target/groovy-gadgeto-scanner-full.jar /usr/app/
COPY ./config /usr/app/config

VOLUME /usr/src

ENTRYPOINT ["/usr/bin/java","-jar","/usr/app/groovy-gadgeto-scanner-full.jar", "--config", "/usr/app/config"]