#!/usr/bin/env bash

# https://github.com/asciidoctor/docker-asciidoctor

DOCKER_IMAGE=asciidoctor/docker-asciidoctor
DOCKER_WORKDIR=/documents

filename=demo

docker run -it \
	-v $(pwd):${DOCKER_WORKDIR}/ \
	-w ${DOCKER_WORKDIR}/target \
    	${DOCKER_IMAGE} \
    	asciidoctor -r asciidoctor-diagram -a sourcedir=${DOCKER_WORKDIR}/src/main/java ${filename}.adoc

docker run -it \
	-v $(pwd):${DOCKER_WORKDIR}/ \
	-w ${DOCKER_WORKDIR}/target \
    	${DOCKER_IMAGE} \
    	asciidoctor-pdf -r asciidoctor-diagram -a sourcedir=${DOCKER_WORKDIR}/src/main/java ${filename}.adoc