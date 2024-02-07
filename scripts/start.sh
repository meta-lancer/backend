#!/bin/bash

ROOT_PATH="/home/ubuntu/backend"
JAR="$ROOT_PATH/meta-lancer-backend-0.0.1-SNAPSHOT.jar"

NOW=$(date +%c)

echo "[$NOW] $JAR 복사"
cp $ROOT_PATH/build/libs/meta-lancer-backend-0.0.1-SNAPSHOT.jar $JAR

echo "[$NOW] > $JAR 실행"
nohup nohup java -Dspring.profiles.active=dev -jar -Duser.timezone=Asis/Seoul $JAR

SERVICE_PID=$(pgrep -f $JAR)
echo "[$NOW] > 서비스 PID: $SERVICE_PID"