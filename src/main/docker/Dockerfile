FROM swr.cn-north-4.myhuaweicloud.com/wz-devops/openjdk:8-jdk-alpine-wz
MAINTAINER liangcmwn

ENV PARAMS=""

VOLUME /usr/app

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD jasperreports-sample.jar /app.jar

ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]