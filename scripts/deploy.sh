#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=woomool-market

cp $REPOSITORY/zip/*.jar $REPOSITORY/

CURRENT_PID=$(pgrep -fl module-api | grep jar | awk '{print $1}')

if [ -z "$CURRENT_PID" ]; then
    echo "> there is no application running"
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

# shellcheck disable=SC2012
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

chmod +x $JAR_NAME

nohup java -jar $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &