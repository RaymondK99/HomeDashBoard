#!/bin/bash

mvn install
docker-compose build
docker-compose down
docker-compose up -d
