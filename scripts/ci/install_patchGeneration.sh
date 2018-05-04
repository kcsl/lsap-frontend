#!/bin/bash

cd `git rev-parse --show-toplevel`/JavaWorkspace/patchGeneration; mvn install -B && cat target/site/jacoco/index.html
