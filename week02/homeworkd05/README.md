# Homework05

**5.（选做）**运行课上的例子，以及 Netty 的例子，分析相关现象。

运行环境
> jdk 11

### HttpServer01
-Xmx512m
curl http://localhost:8801/

wrk -c 40 -d30s http://localhost:8801
Running 30s test @ http://localhost:8801
2个线程    40个连接
2 threads and 40 connections
                                平均值     标准差     最大值     正负一个标准差占比
线程状态          Thread Stats   Avg      Stdev     Max      +/- Stdev
响应时间          Latency        1.37ms  352.91us  19.30ms   86.89%
每线程每秒完成请求 Req/Sec        14.05k     1.25k   16.07k    73.42%

30.10s执行了841325个请求，读取68.29MB数据
841325 requests in 30.10s, 68.29MB read
Socket错误      连接错误数     读错误     写错误         超时数
Socket errors: connect 0, read 312240, write 543024, timeout 0
Requests/sec:  27950.91     //每秒并发请求数
Transfer/sec:      2.27MB   //每秒钟读取2.77MB数据

### HttpServer02
-Xmx512m

wrk -c 40 -d30s http://localhost:8802
Running 30s test @ http://localhost:8802
2 threads and 40 connections
Thread Stats   Avg      Stdev     Max   +/- Stdev
Latency     2.48ms  823.71us  23.89ms   94.21%
Req/Sec     7.87k   721.69    15.90k    90.18%
470671 requests in 30.10s, 38.58MB read
Socket errors: connect 0, read 276695, write 206869, timeout 0
Requests/sec:  15637.23     //每次请求创建一个线程，这样反而对CPU有要求。
Transfer/sec:      1.28MB

### HttpServer03
-Xmx512m

wrk -c 40 -d30s http://localhost:8803
Running 30s test @ http://localhost:8803
2 threads and 40 connections
Thread Stats   Avg      Stdev     Max   +/- Stdev
Latency   428.71us  402.93us  15.74ms   86.08%
Req/Sec    23.20k     1.73k   28.19k    67.50%
1384990 requests in 30.00s, 111.13MB read
Socket errors: connect 0, read 310509, write 1077860, timeout 0
Requests/sec:  46163.56     //使用线程池后，接口性能提升一大截
Transfer/sec:      3.70MB


###　NettyHttpServer
引入依赖
```xml
<!-- https://mvnrepository.com/artifact/io.netty/netty-all -->
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
    <version>4.1.65.Final</version>
</dependency>
```

wrk -c 40 -d30s http://localhost:8808/test
Running 30s test @ http://localhost:8808/test
2 threads and 40 connections
Thread Stats   Avg      Stdev     Max   +/- Stdev
Latency     1.06ms    8.14ms 263.73ms   97.83%
Req/Sec    98.48k    18.79k  126.99k    91.18%
5891140 requests in 30.10s, 623.62MB read
Requests/sec: 195720.17
Transfer/sec:     20.72MB