FROM kalilinux/kali-rolling

# OS update
RUN apt-get update && apt-get -yu dist-upgrade -y

# Install tools
RUN apt-get -y install wget unzip

RUN apt-get install -y openjdk-11-jdk

# Python ENV installation
RUN apt-get install -y python3-pip python3-dev \
  && cd /usr/local/bin \
  && ln -s /usr/bin/python3 python \
  && pip3 install --upgrade pip \
  && apt-get install -y \
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

EXPOSE 50505 # Debug Port

RUN mkdir -p /usr/src
WORKDIR /usr/src



# enable if needed
#COPY requirements.txt .
#RUN pip install -r requirements.txt

VOLUME /usr/src