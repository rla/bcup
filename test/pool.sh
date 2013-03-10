#!/bin/sh

appletviewer -J-Djava.security.policy=all.policy pool1.html &
appletviewer -J-Djava.security.policy=all.policy pool2.html &
appletviewer -J-Djava.security.policy=all.policy pool3.html &

