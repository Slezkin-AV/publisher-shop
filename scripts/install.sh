#!/bin/bash
#set -e

for i in "$@"
do
#  helm -n pub uninstall ${i}
  helm -n pub template ${i} ./${i}/charts > ./${i}/${i}.yaml
  helm -n pub upgrade --install ${i} ./${i}/charts
done
