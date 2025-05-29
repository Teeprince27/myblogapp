FROM amazoncorretto:17-alpine3.21-jdk

LABEL com.temitope.authors="topesamuelajayi@gmail.com"

EXPOSE 8086

ENV VAR_SERVICE_NAME=myblogapp
ENV VAR_DEPLOYMENT_ARTEFACT_PATH=deployment-artefacts

# Combine update, install, upgrade, and user creation in a single layer
RUN apk add --no-cache bash=5.2.37-r0 curl=8.12.1-r1 gzip=1.13-r0 && \
    apk update && \
    apk upgrade && \
    addgroup -S appgroup && \
    adduser -S appuser -G appgroup

RUN if [ ! -d "/app" ]; then mkdir -p /app; fi

# Copy files, set permissions, and run env.sh in a single layer
COPY ./$VAR_DEPLOYMENT_ARTEFACT_PATH/startup.sh /app/$VAR_SERVICE_NAME/startup.sh
COPY ./$VAR_DEPLOYMENT_ARTEFACT_PATH/env.sh /app/$VAR_SERVICE_NAME/env.sh
COPY target/myblogapp-*.jar /opt/$VAR_SERVICE_NAME/myblogapp.jar
RUN chmod +x /app/$VAR_SERVICE_NAME/env.sh && \
    /app/$VAR_SERVICE_NAME/env.sh && \
    chmod +x /app/$VAR_SERVICE_NA                                             ME/startup.sh && \
    chown -R appuser:appgroup /app /opt/$VAR_SERVICE_NAME

WORKDIR /opt/$VAR_SERVICE_NAME

USER appuser


CMD ["/app/myblogapp/startup.sh"]
