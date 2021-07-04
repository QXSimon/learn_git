# Homework02

**2.（选做）**使用压测工具（wrk 或 sb），演练 gateway-server-0.0.1-SNAPSHOT.jar 示例。

执行环境
> jdk 11

同样执行30秒
2线程40个连接， 每秒

### 运行程序
```log
java -jar -Xmx512m -Xms512m gateway-server-0.0.1-SNAPSHOT.jar
```
默认是并行GC-XX:+UseParallelGC

### wrk压测
统一执行30秒

2个线程40个连接
```log
> $ wrk -t 2 -c 40 -d30s http://localhost:8088/api/Hello                                                                                                                                                                                                      [±main ●●]
Running 30s test @ http://localhost:8088/api/Hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     9.61ms   16.86ms 276.90ms   90.71%
    Req/Sec     4.94k     0.98k    6.38k    82.55%
  293249 requests in 30.00s, 75.28MB read
  Non-2xx or 3xx responses: 293249
Requests/sec:   9773.96
Transfer/sec:      2.51MB
```
4个线程40个连接
```log
> $ wrk -t 4 -c 40 -d30s http://localhost:8088/api/Hello                                                                                                                                                                                                      [±main ●●]
Running 30s test @ http://localhost:8088/api/Hello
  4 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     7.75ms   10.90ms 141.54ms   86.84%
    Req/Sec     2.73k   331.12     3.91k    69.92%
  326164 requests in 30.00s, 83.73MB read
  Non-2xx or 3xx responses: 326164
Requests/sec:  10870.34
Transfer/sec:      2.79MB
```
2个线程80个连接
```log
> $ wrk -t 2 -c 80 -d30s http://localhost:8088/api/Hello  
  2 threads and 80 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    17.64ms   25.10ms 411.94ms   86.34%
    Req/Sec     5.48k   469.25     7.00k    69.33%
  327399 requests in 30.03s, 84.05MB read
  Non-2xx or 3xx responses: 327399
Requests/sec:  10901.62
Transfer/sec:      2.80MB
```
4个线程80个连接
```log
> $ wrk -t 4 -c 80 -d30s http://localhost:8088/api/Hello                                                                                                                                                                                                      [±main ●●]
Running 30s test @ http://localhost:8088/api/Hello
  4 threads and 80 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    17.21ms   23.99ms 334.41ms   86.39%
    Req/Sec     2.67k   348.19     4.21k    68.50%
  318535 requests in 30.02s, 81.77MB read
  Non-2xx or 3xx responses: 318535
Requests/sec:  10609.66
Transfer/sec:      2.72MB
```




