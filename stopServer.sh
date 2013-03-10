#!/bin/sh

# Constructs classpath and stops BCup server
# Raivo Laanemets

echo "Stopping server"

CP=dist/bcup-common.jar
CP=$CP:dist/bcup-server.jar

for JAR in `ls lib/client/*.jar`
do
        CP=$CP:$JAR
done

for JAR in `ls lib/server/*.jar`
do
	CP=$CP:$JAR
done

java -cp $CP ee.pri.bcup.server.Shutdown
