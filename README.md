#### BUILD

```
mvn clean install
```

#### RUN
```

#!/bin/bash
cd  $(cd `dirname $0`; pwd)

nohup java -jar imageupload.jar --spring.config.location=application.properties > mylog.log 2>&1 &
```


#### API

```
curl -X POST -F "file=@/Volumes/D/datas/pictures/graylog/2.png;type=image/jpeg" -F 'filename=test.png' -H 'token:8888888' 'localhost:8080/imageUpload' 
```
