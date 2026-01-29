#!/bin/bash

cd "$(dirname "$0")" || exit 1
java -jar "./bin/clientBR-1.0-jar-with-dependencies.jar"
