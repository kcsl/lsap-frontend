#!/bin/bash

cd `git rev-parse --show-toplevel`/JavaWorkspace/rssReaderConcept; mvn install -B && cat target/site/jacoco/index.html
