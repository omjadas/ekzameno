#!/usr/bin/env bash

mvn -Dexec.mainClass="com.ekzameno.ekzameno.shared.Seeds" exec:java
mvn -Dmaven.repo.local=/m2 cargo:run
