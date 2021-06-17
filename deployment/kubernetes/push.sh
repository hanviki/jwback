#!/bin/bash

# #####################################
# Copyright (C) 2020 武汉医路云科技有限公司
# #####################################

set -e -x

# export cf config

if test -f ./ci.cfg ; then
    export $(sed '/^#/d' ./ci.cfg)
else
    echo - e "not exist ./ci.cfg"
fi

docker push ${PROJECT_IMAGE}



