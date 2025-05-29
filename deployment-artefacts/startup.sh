#!/bin/bash

java -jar -Dspring.profiles.active=docker /opt/$VAR_SERVICE_NAME/myblogapp.jar

