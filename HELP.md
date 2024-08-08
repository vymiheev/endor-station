# Getting Started

### Howto
In resources/application.yaml change station-ender.cannon.ion-generation-N.host to <your_host>.

Then build the project and run docker:
```
gradle clean build
docker build -t vymiheev-backend-general-test .
docker run -dp 3000:3000 vymiheev-backend-general-test 
VAR_HOST=<host_name> docker-compose up
```
Run tests:
```
./tests.sh
```