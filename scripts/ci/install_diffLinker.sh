#!/bin/bash

cd `git rev-parse --show-toplevel`/JavaWorkspace/diffLinker; mvn install -B && cat target/site/jacoco/index.html
