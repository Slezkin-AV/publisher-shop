#!/bin/bash

# set -e

for proc in `pgrep -f locust`
do
  echo "killing " `ps u --no-headers -p $proc`
  kill -9 $proc
done

locust --host http://publisher.localdev.me:8000