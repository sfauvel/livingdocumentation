#!/usr/bin/env bash

# https://github.com/asciidoctor/docker-asciidoctor

DOCKER_IMAGE=asciidoctor/docker-asciidoctor
DOCKER_WORKDIR=/documents

FILENAME=demo
TARGET_PATH=target/doc



docker run -it \
	-v $(pwd):${DOCKER_WORKDIR}/ \
	-w ${DOCKER_WORKDIR}/${TARGET_PATH} \
    	${DOCKER_IMAGE} \
    	asciidoctor -r asciidoctor-diagram -a sourcedir=${DOCKER_WORKDIR}/src/main/java  -o ${FILENAME}-full.html --attribute fullDoc ${FILENAME}.adoc

echo "HTML full documentation was generated. You can found it in ${TARGET_PATH}"


docker run -it \
	-v $(pwd):${DOCKER_WORKDIR}/ \
	-w ${DOCKER_WORKDIR}/${TARGET_PATH} \
    	${DOCKER_IMAGE} \
    	asciidoctor -r asciidoctor-diagram -a sourcedir=${DOCKER_WORKDIR}/src/main/java ${FILENAME}.adoc

echo "HTML documentation was generated. You can found it in ${TARGET_PATH}"

docker run -it \
	-v $(pwd):${DOCKER_WORKDIR}/ \
	-w ${DOCKER_WORKDIR}/${TARGET_PATH} \
    	${DOCKER_IMAGE} \
    	asciidoctor-pdf -r asciidoctor-diagram -a sourcedir=${DOCKER_WORKDIR}/src/main/java ${FILENAME}.adoc

echo "PDF documentation was generated. You can found it in ${TARGET_PATH}"