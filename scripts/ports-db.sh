#!/bin/bash

set -e

for proc in `pgrep -f 5432`
do
  echo "killing " `ps -p $proc --no-headers`
  kill -9 $proc
done

kubectl port-forward -n pub $(kubectl -n pub get pod -l 'app.kubernetes.io/name=note' -o name) 5433:5432 &
kubectl port-forward -n pub $(kubectl -n pub get pod -l 'app.kubernetes.io/name=billing' -o name) 5431:5432 &
kubectl port-forward -n pub $(kubectl -n pub get pod -l 'app.kubernetes.io/name=user' -o name) 5430:5432 &
kubectl port-forward -n pub $(kubectl -n pub get pod -l 'app.kubernetes.io/name=order' -o name) 5434:5432 &