#!/bin/bash

DBDIR=/opt/tomcat/tomcat-stage/webapps

sudo rm $DBDIR/database.db
sudo sqlite3 $DBDIR/database.db -init src/main/resources/database.sql
