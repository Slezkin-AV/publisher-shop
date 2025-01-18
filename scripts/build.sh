
Red='\033[0;31m'
Green='\033[0;32m'
Purple='\033[0;35m'
NC='\033[0m' # No Color
Bold='\033[1m'
#echo -e "I ${RED}love${NC} Stack Overflow"

echo
echo -e "[${Purple}INFO${NC}] ${Bold}----------------------< ${Green}docker build${NC}${Bold} >---------------------"
#echo `pwd`, $1, $2
echo docker buildx build . -t "slezkin71/microservice:$1_$2"
echo docker push "slezkin71/microservice:$1_$2"
echo
echo -e "[${Purple}INFO${NC}] ${Bold}-----------------< ${Green}docker build complete${NC}${Bold} >-----------------"
echo

#echo "================================"
#
#echo helm uninstall publisher -n pub
#echo "================================"
#echo
#sleep 1
#docker buildx build . -t "slezkin71/microservice:publisher_v1"
#sleep 1
#echo "================================"
#echo
#docker push "slezkin71/microservice:publisher_v1"
#echo "================================"
#echo
#sleep 1
#helm install publisher publisher-chart -n pub
#echo "================================"
#echo `date`
#echo