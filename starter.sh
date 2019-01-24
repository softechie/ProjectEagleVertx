#!/usr/bin/env bash
mvn -f app/ clean package ; java -jar app/target/vertx-learning-1.0-SNAPSHOT-fat.jar