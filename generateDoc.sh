#!/usr/bin/env bash

mvn install


mvn exec:java -D"exec.mainClass"="org.dojo.livingdoc.DemoDocumentation"

. ./convertAdoc.sh