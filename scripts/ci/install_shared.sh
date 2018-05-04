#!/bin/bash

cd `git rev-parse --show-toplevel`/JavaWorkspace/shared; mvn install -B && cat target/site/jacoco/index.html
