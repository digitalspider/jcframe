#!/bin/bash

#mvn -f ../pom.xml clean install

TOMCATDIR=/opt/tomcat/tomcat-stage

rm -rf $TOMCATDIR/webapps/site*
sleep 2
cp target/site.war $TOMCATDIR/webapps/
tail -100f $TOMCATDIR/logs/catalina.out 
