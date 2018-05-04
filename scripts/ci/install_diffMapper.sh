#!/bin/bash

cd `git rev-parse --show-toplevel`/JavaWorkspace/diffMapper; mvn install -B && cat target/site/jacoco/index.html
