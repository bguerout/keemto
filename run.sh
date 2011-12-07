#!/bin/bash

echo Installing into Maven Repository Exchange WebService API
cd modules/ews-java
mvn clean install

echo Installing into Maven Repository Spring Social Yammer
cd ../spring-yammer-social
./gradlew install

echo Running Keemto Application
cd ../..
mvn clean install jetty:run-war
