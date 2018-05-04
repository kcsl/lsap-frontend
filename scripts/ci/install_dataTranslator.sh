#!/bin/bash

cd `git rev-parse --show-toplevel`/JavaWorkspace/dataTranslator; mvn install -B && cat target/site/jacoco/index.html
