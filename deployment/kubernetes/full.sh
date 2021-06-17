#!/bin/bash

# #####################################
# Copyright (C) 2020 武汉医路云科技有限公司
# #####################################

set -e -x

# export run env

export RUNTIME_MARK=$1
export BRANCH_MARK=$2
export MANIFEST_SKIP=$3
export DEPLOYMENT_EXEC=$4

if test -z ${RUNTIME_MARK} ; then
   export RUNTIME_MARK=dev
fi

if test -z ${MANIFEST_SKIP} ; then
   export MANIFEST_SKIP=no
fi

if test -z ${BRANCH_MARK} ; then
   export BRANCH_MARK=any
fi

if test "${RUNTIME_MARK}"x == "run"x ; then

    echo -e "RUNTIME_MARK can't be set to [run]"
    exit 1

fi

echo -e
echo -e "RUNTIME_MARK value : ${RUNTIME_MARK}"
echo -e "MANIFEST_SKIP value : ${MANIFEST_SKIP}"
echo -e "DEPLOYMENT_EXEC value : ${DEPLOYMENT_EXEC}"
echo -e "BRANCH_MARK value : ${BRANCH_MARK}"
echo -e

# export cf config

if test -f ./ci.cfg ; then
    export $(sed '/^#/d' ./ci.cfg)
else
    echo -e "not exist ./ci.cfg"
fi

# export target config

if test -f ./env/${RUNTIME_MARK}/env.cfg ; then
    export $(sed '/^#/d' ./env/${RUNTIME_MARK}/env.cfg)
else
    echo -e "./env/${RUNTIME_MARK}/env.cfg is not exist"
fi

# build run dir

mkdir -p ./run/

# export run config

if test "${RUNTIME_MARK}"x != "prod"x ; then
    echo -e "PROJECT_DOMAIN=${RUNTIME_MARK}.$PROJECT_DOMAIN" >> ./run/env.cfg
    echo -e "PROJECT_NAMESPACE=${RUNTIME_MARK}-$PROJECT_NAMESPACE" >> ./run/env.cfg
else
    echo -e "PROJECT_DOMAIN=$PROJECT_DOMAIN" >> ./run/env.cfg
    echo -e "PROJECT_NAMESPACE=$PROJECT_NAMESPACE" >> ./run/env.cfg
fi

export $(sed '/^#/d' ./run/env.cfg)

# export branch config

if test "${BRANCH_MARK}"x != "any"x; then
    echo -e "PROJECT_DOMAIN=${BRANCH_MARK}.$PROJECT_DOMAIN" > ./run/branch.cfg
    echo -e "PROJECT_NAMESPACE=${BRANCH_MARK}-$PROJECT_NAMESPACE" >> ./run/branch.cfg
fi

export $(sed '/^#/d' ./run/branch.cfg)

# build k8s tmp dir

mkdir -p ./tmp

YAML_TARGET_FILENAME=${PROJECT_NAME}-${RUNTIME_MARK}-${BRANCH_MARK}

# build k8s manifest yml

if test "${MANIFEST_SKIP}"x != "yes"x ; then
    echo -e > ./tmp/${YAML_TARGET_FILENAME}.yml
    envsubst < ./app/manifest.yml >> ./tmp/${YAML_TARGET_FILENAME}.yml
fi

# build k8s app yml

for yml in `ls ./app | grep yml`;do
    if test "${yml}"x != "manifest.yml"x ; then
        echo -e >> ./tmp/${YAML_TARGET_FILENAME}.yml
        envsubst < ./app/${yml} >> ./tmp/${YAML_TARGET_FILENAME}.yml
    fi
done

# build k8s env yml
# check k8s env dir

if test -d ./env/${RUNTIME_MARK} ; then
    for yml in `ls ./env/${RUNTIME_MARK} | grep yml`;do
        echo -e >> ./tmp/${YAML_TARGET_FILENAME}.yml
        envsubst < ./env/${RUNTIME_MARK}/${yml} >> ./tmp/${YAML_TARGET_FILENAME}.yml
    done
fi

# build k8s run yml

for yml in `ls ./run | grep yml`;do
    echo -e >> ./tmp/${YAML_TARGET_FILENAME}.yml
    envsubst < ./run/${yml} >> ./tmp/${YAML_TARGET_FILENAME}.yml
done

# deploy k8s action

echo -e

if test "${DEPLOYMENT_EXEC}"x == "install"x ; then
    echo -e "exec deploy k8s install action"
    kubectl apply -f ./tmp/${YAML_TARGET_FILENAME}.yml
elif test "${DEPLOYMENT_EXEC}"x == "uninstall"x ; then
    echo -e "exec deploy k8s uninstall action"
    kubectl delete -f ./tmp/${YAML_TARGET_FILENAME}.yml
else
    echo -e "skip deploy k8s action"
fi

echo -e
