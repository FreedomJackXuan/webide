#!/bin/bash
#docker run -it -d -v $1:$2 ubuntu:15.10 /bin/bash
#docker run -d -v $1:$2 python /bin/sh -c "while true; do echo hello world; sleep 100;done"
#docker start $(docker ps -l)
#docker run -d -p 127.0.0.1:8989:5555 -v /home/jingbao/桌面/MAC:/home -ip 172.17.0.10 mypython /bin/sh -c "while true; do echo hello world; sleep 100;done"

docker run -d -p 127.0.0.1:$1:5555 -v $2:$3 mypython /bin/sh -c "while true; do echo hello world; sleep 100;done"
echo $(docker ps -l)
