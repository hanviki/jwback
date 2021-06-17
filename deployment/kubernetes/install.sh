#!/bin/bash

# #####################################
# Copyright (C) 2020 武汉医路云科技有限公司
# #####################################

set -e -x

export RUNTIME_MARK="$1"
export BRANCH_MARK="$2"

if test -z ${RUNTIME_MARK} ; then
   export RUNTIME_MARK=dev
fi

if test -z ${BRANCH_MARK} ; then
   export BRANCH_MARK=any
fi

./full.sh "$RUNTIME_MARK" "$BRANCH_MARK" "no" "install"
