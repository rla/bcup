#!/bin/sh

# Constructs classpath and starts BCup server
# Raivo Laanemets

touch server.lock

CP=dist/bcup-common.jar
CP=$CP:dist/bcup-server.jar

for JAR in `ls lib/server/*.jar`
do
	CP=$CP:$JAR
done

for JAR in `ls lib/client/*.jar`
do
	CP=$CP:$JAR
done

java -cp $CP ee.pri.bcup.server.Startup

rm server.lock
