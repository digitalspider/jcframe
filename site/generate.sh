#!/bin/bash

if [[ -d site/target ]] ; then
  cd site
fi

java -cp target/classes/:target/site-bin.jar au.com.jcloud.jcframe.Generator $1

