#!/bin/bash

if [[ -d site/target ]] ; then
  cd site
fi

java -cp target/classes/:target/site-bin.jar au.com.javacloud.jcframe.Generator $1

