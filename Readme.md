Visage Cloud - EK stack API POC
---


1. Build docker image
```
docker build -t visagecloud/es-poc .
```

2. Run latest docker container
```
docker run -d -p 10002:9200 -p 10001:5601 visagecloud/es-poc
```

3. Go To [Kibana](http://localhost:10001)
3.1 For Docker Toolbox [Kibana](http://192.168.99.100:10001)


Resources
https://bitbucket.org/nshou/elasticsearch-kibana