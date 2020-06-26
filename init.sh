#!/bin/bash

echo "Building the project and the docker image"

mvn clean install

docker build -t gadjetoscanner .

echo "Done"
echo "Run it as follows"
echo "docker run -p 5050:5050 --rm -it gadjetoscanner --phase <phase> --url <target>"