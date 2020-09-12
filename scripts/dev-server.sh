#!/usr/bin/env bash

/app/scripts/wait-for-it.sh postgres:5432 -s -t 120

mvn -Dmaven.repo.local=/m2 -Dexec.mainClass="com.ekzameno.ekzameno.shared.Seeds" exec:java
mvn -Dmaven.repo.local=/m2 cargo:run
