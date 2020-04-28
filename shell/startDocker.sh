#!/bin/bash
docker run -d -p 127.0.0.1::5555 -v $1:$2 --name=$3 mypython /bin/bash