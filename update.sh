#!/bin/sh

# Updates BCUP server.

# Stop old server.

if [ -e server.lock ]
then
	./stopServer.sh
fi

MAX_WAIT=30
CUR_WAIT=0
while [ -e server.lock ]
do
	echo "Waiting server shutdown"
	sleep 1
	CUR_WAIT=$(( $CUR_WAIT + 1 ))
	if [ $CUR_WAIT -gt $MAX_WAIT ]
	then
		break
	fi
done

if [ -e server.lock ]
then
	echo "Error: server lock still exist, cannot update"
	exit 1
fi

./startServer.sh
