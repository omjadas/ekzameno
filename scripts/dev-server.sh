#!/usr/bin/env bash

mvn -Dmaven.repo.local=/m2 -Dexec.mainClass="com.ekzameno.ekzameno.shared.Seeds" exec:java
mvn -Dmaven.repo.local=/m2 cargo:run
