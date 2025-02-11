#!/bin/bash

# set -e

for proc in `pgrep -f "port-forward -n (kube|ingress|kafka)"`
do
  echo "killing " `ps -f --no-headers -p $proc`
  kill -9 $proc
done


kubectl port-forward -n ingress-nginx daemonset/ingress-nginx-controller 8000:80 &
kubectl port-forward -n kube-prometheus-stack service/kube-prometheus-stack-grafana 8080:80 &
kubectl port-forward -n kube-prometheus-stack service/kube-prometheus-stack-prometheus 9090:9090 &
kubectl port-forward -n kafka-ca1 service/kafka-manager 9000 &

# kubectl port-forward -n ingress-nginx service/ingress-nginx-controller 8000:80 &
# kubectl port-forward -n monitoring service/kube-prometheus-stack-grafana 8080:80 &
# kubectl port-forward -n monitoring service/kube-prometheus-stack-prometheus 9090:9090 &
# minikube service -n kafka-ca1 kafka-manager --url &
#kubectl port-forward -n kafka-ca1 service/kafka-manager 9090:9090 &