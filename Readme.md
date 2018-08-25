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


Resources
https://bitbucket.org/nshou/elasticsearch-kibana