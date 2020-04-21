#!/bin/bash
docker run -d -p 127.0.0.1:$1:5555 -v $2:$3 mypython /bin/bash -c "while true; do sleep 1;done"