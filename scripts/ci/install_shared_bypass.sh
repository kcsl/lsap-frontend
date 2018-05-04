#!/bin/bash

cd `git rev-parse --show-toplevel`/JavaWorkspace/shared; mvn install -B -Dmaven.test.skip=true
