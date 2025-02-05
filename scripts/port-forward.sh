#!/bin/bash

set -e

for proc in `pgrep -f port-forward`
do
  echo "killing " `ps u --no-headers -p $proc`
  echo kill -9 $proc
done

kubectl port-forward -n ingress-nginx service/ingress-nginx-controller 8000:80 &
kubectl port-forward -n monitoring service/kube-prometheus-stack-grafana 8080:80 &
kubectl port-forward -n monitoring service/kube-prometheus-stack-prometheus 9090:9090 &
# minikube service -n kafka-ca1 kafka-manager --url &
#kubectl port-forward -n kafka-ca1 service/kafka-manager 9090:9090 &