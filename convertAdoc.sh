#!/usr/bin/env bash

# https://github.com/asciidoctor/docker-asciidoctor

DOCKER_IMAGE=asciidoctor/docker-asciidoctor
DOCKER_WORKDIR=/documents

FILENAME=demo
TARGET_PATH=target/docs
ASCIIDOC_PATH=target/adoc
DOC_PATH=docs

if [ ! -d ${ASCIIDOC_PATH} ]
then
    echo "Directory ${ASCIIDOC_PATH} does not exists."
    return
fi

function generate() {
    COMMAND="$@"

    docker run -it \
        -v $(pwd):${DOCKER_WORKDIR}/ \
        -w ${DOCKER_WORKDIR} \
            ${DOCKER_IMAGE} \
                $COMMAND \
                -r asciidoctor-diagram \
                -a sourcedir=${DOCKER_WORKDIR}/src/main/java \
                -a webfonts! \
                ${ASCIIDOC_PATH}/${FILENAME}.adoc
}


if [ ! -d ${DOC_PATH} ]
then
    mkdir ${DOC_PATH}
fi

generate asciidoctor -D ${DOC_PATH} -o index.html --attribute fullDoc
echo "HTML index documentation was generated as ${DOC_PATH}/index.html"

if [ ! -d ${TARGET_PATH} ]
then
    mkdir ${TARGET_PATH}
fi

generate asciidoctor -D ${TARGET_PATH} -o ${FILENAME}-full.html --attribute fullDoc
echo "HTML full documentation was generated as ${TARGET_PATH}/${FILENAME}-full.html"

generate asciidoctor -D ${TARGET_PATH}
echo "HTML documentation was generated as ${TARGET_PATH}/${FILENAME}.html"

# Must generate html before to create images
generate asciidoctor-pdf  -B ${TARGET_PATH} -o ${FILENAME}-full.pdf --attribute fullDoc
echo "PDF documentation was generated as ${TARGET_PATH}/${FILENAME}.pdf"
