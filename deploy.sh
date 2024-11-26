PROJECT_NETWORK='project1-network'
SERVER_IMAGE='project1-server-image'
SERVER_CONTAINER='my-server'
CLIENT_IMAGE='project1-client-image'
CLIENT_CONTAINER='my-client'

# clean up existing resources, if any
echo "----------Cleaning up existing resources----------"
docker container stop $SERVER_CONTAINER 2> /dev/null && docker container rm $SERVER_CONTAINER 2> /dev/null
docker container stop $CLIENT_CONTAINER 2> /dev/null && docker container rm $CLIENT_CONTAINER 2> /dev/null
docker network rm $PROJECT_NETWORK 2> /dev/null

# only cleanup
if [ "$1" == "cleanup-only" ]
then
  exit
fi

# create a custom virtual network
echo "----------creating a virtual network----------"
docker network create $PROJECT_NETWORK

# build the images from Dockerfile
echo "----------Building images----------"
docker build -t $CLIENT_IMAGE --target client-build .
docker build -t $SERVER_IMAGE --target server-build .

# Determine protocol (TCP or UDP)
if [ "$2" == "udp" ]; then
  PROTOCOL="UDP"
  PORT="5555"
else
  PROTOCOL="TCP"
  PORT="1111"
fi

# run the image and open the required ports
echo "----------Running sever app----------"
docker run -d -p 1111:1111/tcp -p 5555:5555/udp --name $SERVER_CONTAINER --network $PROJECT_NETWORK $SERVER_IMAGE \
  java -cp out server.ServerApp $PORT $PROTOCOL

echo "----------watching logs from server----------"
docker logs $SERVER_CONTAINER -f
