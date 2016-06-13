#!/bin/bash

cd ..
mvn clean install
cd site
java -cp target/classes/:target/site-full.jar au.com.javacloud.util.Generator

