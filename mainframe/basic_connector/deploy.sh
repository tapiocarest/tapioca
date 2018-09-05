#!/bin/bash

date --iso-8601=seconds
mvn clean package -q
date --iso-8601=seconds
scp target/rest.tapioca-mainframe-basic_connector-0.0.1.jar admin@tapioca:/opt/tapioca/custom_assertions
date --iso-8601=seconds
