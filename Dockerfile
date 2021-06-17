FROM registry.medway.cloud/containers/openjdk:8u212-jdk-alpine

ARG PROJECT_NAME="performance-appraisal-server"

ARG PROJECT_DIST="performance-appraisal-server"

COPY ./target/${PROJECT_DIST}.jar /opt/app/${PROJECT_NAME}/bin/web.jar

WORKDIR /opt/app/${PROJECT_NAME}

CMD [ \
    "/sbin/tini", "--", \
    "bash", "-c", \
    "java $JAVA_OPTS \
    -Dfile.encoding=UTF-8 \
    -Djava.io.tmpdir=/tmp/ \
    -Duser.timezone=Asia/Shanghai \
    -Djava.library.path=/usr/local/lib/ \
    -Djava.security.egd=file:/dev/./urandom \
    -jar bin/web.jar" \
    ]
