#!/usr/bin/env bash

# https://github.com/asciidoctor/docker-asciidoctor

DOCKER_IMAGE=asciidoctor/docker-asciidoctor
DOCKER_WORKDIR=/documents

FILENAME=demo
TARGET_PATH=docs

if [ ! -d ${TARGET_PATH} ]
then
    echo "Directory ${TARGET_PATH} does not exists."
    return
fi

function generate() {
    COMMAND="$@"

    docker run -it \
        -v $(pwd):${DOCKER_WORKDIR}/ \
        -w ${DOCKER_WORKDIR}/${TARGET_PATH} \
            ${DOCKER_IMAGE} \
                $COMMAND \
                -r asciidoctor-diagram \
                -a sourcedir=${DOCKER_WORKDIR}/src/main/java \
                -a webfonts! \
                -D . \
                ${FILENAME}.adoc
}

generate asciidoctor -o ${FILENAME}-full.html --attribute fullDoc
echo "HTML full documentation was generated as ${TARGET_PATH}/${FILENAME}-full.html"

generate asciidoctor
echo "HTML documentation was generated as ${TARGET_PATH}/${FILENAME}.html"

generate asciidoctor-pdf
echo "PDF documentation was generated as ${TARGET_PATH}/${FILENAME}.pdf"

