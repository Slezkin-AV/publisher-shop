#!/bin/bash
set -e

Red='\033[0;31m'
Green='\033[0;32m'
Purple='\033[0;35m'
NC='\033[0m' # No Color
Bold='\033[1m'
BGreen='\033[1;32m'
BPurple='\033[1;35m'
#echo -e "I ${RED}love${NC} Stack Overflow"

echo
echo -e "[${Purple}INFO${NC}] ${Bold}----------------------< ${BPurple}$1_$2: ${Green}docker build ${NC}${Bold} >---------------------${NC}"
mv $3/$4.jar $3/app.jar

docker buildx build . -t "slezkin71/microservice:$1_$2" --progress=auto
echo
echo -e "[${Purple}INFO${NC}] ${Bold} `date` ${NC}"
echo -e "[${Purple}INFO${NC}] ${Bold}-----------------< ${BPurple}$1_$2: ${Green}docker build complete${NC}${Bold} >-----------------${NC}"

echo -e "[${Purple}INFO${NC}] ${Bold}----------------------< ${BPurple}$1_$2: ${Green}pushing image${NC}${Bold} >---------------------${NC}"
docker push "slezkin71/microservice:$1_$2"
echo
echo -e "[${Purple}INFO${NC}] ${Bold} `date` ${NC}"
echo -e "[${Purple}INFO${NC}] ${Bold}----------------------< ${BPurple}$1_$2: ${Green}pushed${NC}${Bold} >---------------------${NC}"
echo
