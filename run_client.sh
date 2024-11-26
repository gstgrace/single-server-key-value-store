CLIENT_IMAGE='project1-client-image'
PROJECT_NETWORK='project1-network'
SERVER_CONTAINER='my-server'

if [ $# -ne 3 ]
then
  echo "Usage: ./run_client.sh <container-name> <port-number> <protocol>"
  exit 1
fi

# Assign the arguments to variables
CONTAINER_NAME="$1"
PORT_NUMBER="$2"
PROTOCOL="$3"

# Run client Docker container with the provided arguments
docker run -it --rm --name "$CONTAINER_NAME" \
  --network $PROJECT_NETWORK $CLIENT_IMAGE \
  java -cp out client.ClientApp "$SERVER_CONTAINER" "$PORT_NUMBER" "$PROTOCOL"