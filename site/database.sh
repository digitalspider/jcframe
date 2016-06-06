#!/bin/bash

rm -f database.db
echo ".exit" | sqlite3 database.db -init src/main/resources/database.sql

