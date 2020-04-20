#!/bin/bash
docker exec $1 kill $(pidof python3.7 $2)
#ps -ef | grep python | awk '{kill $2}'
