#!/bin/bash

# #####################################
# Copyright (C) 2020 武汉医路云科技有限公司
# #####################################

set -e -x

export RUNTIME_MARK="$1"

if test -z ${RUNTIME_MARK} ; then
   export RUNTIME_MARK=dev
fi

./full.sh "$RUNTIME_MARK" "yes" "uninstall"
