docker build -t android-api-server .
docker stack deploy -c docker-compose.yml android-api
