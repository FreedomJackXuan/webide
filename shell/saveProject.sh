#!/bin/bash
for i in $@
        do
            if [ -f $i ];then
        echo "1"
else
        echo "0"
        touch $i
        fi
                done
