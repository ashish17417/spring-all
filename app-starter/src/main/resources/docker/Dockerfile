FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER Ashish Ingle <ashishgajmahi@gmail.com>
VOLUME /tmp
ADD app-starter-docker-deployer-IMAGE_VERSION.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
