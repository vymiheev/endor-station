# Getting Started

### Howto
In resources/application.yaml and in docker-compose.yml swap 192.168.0.170 with your ip.

Then build the project and run docker:
```
gradle clean build
docker build -t vymiheev-backend-general-test .
docker run -dp 3000:3000 vymiheev-backend-general-test 
docker-compose up
```
Run tests:
```
./tests.sh
```