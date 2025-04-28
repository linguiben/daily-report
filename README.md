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
[root@JupiterSo jdk]# export JAVA_HOME=/opt/jdk/jdk-11.0.26;
[root@JupiterSo jdk]# export PATH=$JAVA_HOME/bin:/opt/mvn/apache-maven-3.9.9/bin:/root/.local/bin:/root/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:.

cd /opt/git/repo/daily-report
git clone https://github.com/linguiben/daily-report.git
mvn clean install -f webbora-daily-report-dependencies/pom.xml
mvn clean install
#强制用远程版本覆盖本地
#git fetch origin
#git reset --hard origin/your-branch-name
#git clean -fd
#git pull

docker rm webbora
docker rmi webbora:1.0
docker build -t webbora:1.0 -f ./Dockerfile .
docker images | grep webbora

docker run -d --name wpadmin --hostname wpadmin -p 8090:8090 --network=data_default -v /data/logs/wpadmin:/data/logs/wpadmin -v /data/wpadmin:/usr/apps/wp/data --restart=always world-peace-admin:1.0
docker run -d --name webbora --hostname webbora -m 512m -p 8080:8080 webbora:1.0
docker ps
docker logs -f webbora
curl localhost:8080
```