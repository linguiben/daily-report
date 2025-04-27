FROM openjdk:11-jre-slim
ENV APPS_HOME=/usr/apps/webbora
RUN mkdir -p $APPS_HOME
USER root
WORKDIR $APPS_HOME
VOLUME $APPS_HOME
ADD ./webbora-daily-report-service/target/webbora-service.jar $APPS_HOME/service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/apps/webbora/service.jar"]
MAINTAINER Webbora
