Compile and execute 3 instances:

java -jar -Dserver.port=8081 target/redis-distributed-lock-0.0.1-SNAPSHOT.jar

java -jar -Dserver.port=8082 target/redis-distributed-lock-0.0.1-SNAPSHOT.jar

java -jar -Dserver.port=8083 target/redis-distributed-lock-0.0.1-SNAPSHOT.jar


Analyze DemoController class.

Call http://localhost:8081/lock, http://localhost:8082/lock, http://localhost:8083/lock
using postman or any HTTP Client.

DemoController.lock() HTTP handler method is using Distributed Lock with Redis, each call
will be executed by just 1 thread at a time, see console logs of each process.

