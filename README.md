## webbora-daily-report

Webbora = James Webb Space Telescope + Aurora = web report  
***象征用韦伯望远镜发现了曙光女神。***

## Modules
```java
1. dependencies: 负责依赖管理
2. admin: 负责excel upload, download, delete and maintain
3. common: 负责database, util, config
4. service: 负责web, aop
```
```shell
cd /opt/git/repo/daily-report
git clone https://github.com/linguiben/daily-report.git
mvn clean install -f webbora-daily-report-dependencies/pom.xml
mvn clean install

docker build -t webbora:1.0 -f ./Dockerfile .
docker images | grep webbora
docker run -d --network=wp_net --name wpapi --hostname wpapi -p 8081:8081 world-peace-api:1.0

docker run -d --name webbora --hostname webbora -m 128m -p 8080:8080 webbora:1.0
docker ps
docker logs
curl localhost:8080
```