# 不同堆内存，不同GC的压测表现


## 总结

1.串行GC
64m堆内存， 每秒并发请求数8757.43，每秒读取内存2.25MB
32m堆内存， 每秒并发请求数4775.58，每秒读取内存1.23MB

2.并行GC
64m堆内存， 每秒并发请求数6982.23，每秒读取内存 1.79MB
32m堆内存， 每秒并发请求数 5013.55，每秒读取内存1.29MB

3.CMS GC
64m堆内存， 每秒并发请求数7493.06，每秒读取内存1.92MB
32m堆内存， 每秒并发请求数5858.54，每秒读取内存 1.50MB

4.G1 GC
64m堆内存， 每秒并发请求数7379.87，每秒读取内存1.89MB
32m堆内存， 每秒并发请求数3024.95，每秒读取内存795.20KB

在控制Xmx堆内存不发生OOM的情况下
四种GC在64m内存时，接口性能都差不多。可能与程序复杂度的关系，其中串行GC的吞吐量反而是最好的。
在内存设置到32m寻，只有G1GC，性能退化严重，吞吐量最低，内存读取情况退到795k。
每一种GC在内存缩水后，GC的频率都提高了，同时发生了更多的Full GC。


## 测试数据

### 1.串行GC
> $ java -jar -Xmx64m -Xms64m -XX:+UseSerialGC -XX:+PrintGCDetails gateway-server-0.0.1-SNAPSHOT.jar
压测指令
```bash
wrk -c 40 -d30s http://localhost:8088/api/Hello
```

```log
Running 30s test @ http://localhost:8088/api/Hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    10.43ms   17.62ms 266.16ms   90.01%
    Req/Sec     4.43k     0.97k    6.11k    78.02%
  262795 requests in 30.01s, 67.46MB read
  Non-2xx or 3xx responses: 262795
Requests/sec:   8757.43
Transfer/sec:      2.25MB
```
```log
[Full GC (Metadata GC Threshold) [Tenured: 4500K->5290K(43712K), 0.0258596 secs] 18967K->5290K(63360K), [Metaspace: 20530K->20530K(1067008K)], 0.0259165 secs] [Times: user=0.08 sys=0.00, real=0.03 secs] 
[GC (Allocation Failure) [DefNew: 17472K->1227K(19648K), 0.0035231 secs] 22762K->6518K(63360K), 0.0035686 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
```

>  $ java -jar -Xmx32m -Xms32m -XX:+UseSerialGC -XX:+PrintGCDetails gateway-server-0.0.1-SNAPSHOT.jar
压测指令
```bash
wrk -c 40 -d30s http://localhost:8088/api/Hello
```

```log
Running 30s test @ http://localhost:8088/api/Hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    14.04ms   23.69ms 428.76ms   92.45%
    Req/Sec     2.42k   534.81     3.24k    77.68%
  143322 requests in 30.01s, 36.79MB read
  Non-2xx or 3xx responses: 143322
Requests/sec:   4775.58
Transfer/sec:      1.23MB
```
```log
[GC (Allocation Failure) [DefNew: 8831K->107K(9792K), 0.0018259 secs][Tenured: 21878K->20103K(21888K), 0.0440540 secs] 30708K->20103K(31680K), [Metaspace: 37098K->37098K(1083392K)], 0.0459754 secs] [Times: user=0.05 sys=0.00, real=0.04 secs] 
[GC (Allocation Failure) [DefNew: 8704K->72K(9792K), 0.0019521 secs] 28807K->20176K(31680K), 0.0019786 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 

```





### 2.并行GC

> $ java -jar -Xmx64m -Xms64m -XX:+UseParallelGC -XX:+PrintGCDetails gateway-server-0.0.1-SNAPSHOT.jar
压测指令
```bash
wrk -c 40 -d30s http://localhost:8088/api/Hello
```
```log
Running 30s test @ http://localhost:8088/api/Hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    11.61ms   19.20ms 318.28ms   90.55%
    Req/Sec     3.54k     0.91k    4.82k    73.40%
  209553 requests in 30.01s, 53.80MB read
  Non-2xx or 3xx responses: 209553
Requests/sec:   6982.23
Transfer/sec:      1.79MB
```
```log
[GC (Allocation Failure) [PSYoungGen: 20672K->256K(20992K)] 64583K->44231K(65024K), 0.0015417 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
[Full GC (Ergonomics) [PSYoungGen: 256K->0K(20992K)] [ParOldGen: 43975K->21508K(44032K)] 44231K->21508K(65024K), [Metaspace: 37169K->37169K(1083392K)], 0.0265512 secs] [Times: user=0.08 sys=0.00, real=0.02 secs] 
```

> $ java -jar -Xmx32m -Xms32m -XX:+UseParallelGC -XX:+PrintGCDetails gateway-server-0.0.1-SNAPSHOT.jar
压测指令
```bash
wrk -c 40 -d30s http://localhost:8088/api/Hello
```
```log
Running 30s test @ http://localhost:8088/api/Hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    14.48ms   23.01ms 340.92ms   90.54%
    Req/Sec     2.54k   704.73     3.94k    68.12%
  150455 requests in 30.01s, 38.62MB read
  Non-2xx or 3xx responses: 150455
Requests/sec:   5013.55
Transfer/sec:      1.29MB
```
```log
[GC (Allocation Failure) [PSYoungGen: 9952K->256K(10240K)] 31859K->22227K(32256K), 0.0012680 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[Full GC (Ergonomics) [PSYoungGen: 256K->0K(10240K)] [ParOldGen: 21971K->18940K(22016K)] 22227K->18940K(32256K), [Metaspace: 37087K->37087K(1083392K)], 0.0226005 secs] [Times: user=0.08 sys=0.00, real=0.02 secs]
```

### CMS GC

> $ java -jar -Xmx64m -Xms64m -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails gateway-server-0.0.1-SNAPSHOT.jar
压测指令
```bash
wrk -c 40 -d30s http://localhost:8088/api/Hello
```
```log
Running 30s test @ http://localhost:8088/api/Hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    10.51ms   17.52ms 345.14ms   90.98%
    Req/Sec     3.79k     0.97k    5.06k    74.50%
  224852 requests in 30.01s, 57.72MB read
  Non-2xx or 3xx responses: 224852
Requests/sec:   7493.06
Transfer/sec:      1.92MB
```
```log
[GC (Allocation Failure) [ParNew: 17569K->182K(19648K), 0.0046367 secs] 46350K->28965K(63360K), 0.0046918 secs] [Times: user=0.04 sys=0.00, real=0.01 secs] 
[GC (CMS Initial Mark) [1 CMS-initial-mark: 28783K(43712K)] 28999K(63360K), 0.0005937 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[CMS-concurrent-mark-start]
[GC (Allocation Failure) [ParNew: 17654K->169K(19648K), 0.0008515 secs] 46437K->28952K(63360K), 0.0008875 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[CMS-concurrent-mark: 0.028/0.030 secs] [Times: user=0.18 sys=0.00, real=0.03 secs] 
[GC (Allocation Failure) [ParNew: 17641K->219K(19648K), 0.0009917 secs] 46424K->29003K(63360K), 0.0010198 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
[CMS-concurrent-preclean-start]
[CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC (CMS Final Remark) [YG occupancy: 2135 K (19648 K)][Rescan (parallel) , 0.0008007 secs][weak refs processing, 0.0000332 secs][class unloading, 0.0075980 secs][scrub symbol table, 0.0039841 secs][scrub string table, 0.0006003 secs][1 CMS-remark: 28784K(43712K)] 30919K(63360K), 0.0131101 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
[CMS-concurrent-sweep-start]
[GC (Allocation Failure) [ParNew: 17691K->175K(19648K), 0.0132300 secs] 43573K->26058K(63360K), 0.0132787 secs] [Times: user=0.10 sys=0.00, real=0.02 secs] 
[CMS-concurrent-sweep: 0.014/0.028 secs] [Times: user=0.22 sys=0.00, real=0.02 secs] 
[GC (Allocation Failure) [ParNew: 17647K->152K(19648K), 0.0009869 secs] 39228K->21736K(63360K), 0.0010276 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
[CMS-concurrent-reset-start]
[CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
```

> $ java -jar -Xmx32m -Xms32m -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails gateway-server-0.0.1-SNAPSHOT.jar
压测指令
```bash
wrk -c 40 -d30s http://localhost:8088/api/Hello
```
```log
Running 30s test @ http://localhost:8088/api/Hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    12.03ms   21.54ms 499.41ms   92.91%
    Req/Sec     2.96k     0.91k    4.19k    75.34%
  175813 requests in 30.01s, 45.13MB read
  Non-2xx or 3xx responses: 175813
Requests/sec:   5858.54
Transfer/sec:      1.50MB
```
```log
[GC (Allocation Failure) [ParNew: 8801K->109K(9792K), 0.0008184 secs] 28930K->20250K(31680K), 0.0008606 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
[GC (CMS Initial Mark) [1 CMS-initial-mark: 20140K(21888K)] 20374K(31680K), 0.0005679 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[CMS-concurrent-mark-start]
[GC (Allocation Failure) [ParNew: 8813K->100K(9792K), 0.0006708 secs] 28954K->20244K(31680K), 0.0007033 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
[GC (Allocation Failure) [ParNew: 8804K->94K(9792K), 0.0008312 secs] 28948K->20243K(31680K), 0.0008670 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC (Allocation Failure) [ParNew: 8798K->81K(9792K), 0.0008491 secs] 28947K->20230K(31680K), 0.0008903 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
[GC (Allocation Failure) [ParNew: 8785K->99K(9792K), 0.0006987 secs] 28934K->20249K(31680K), 0.0007517 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
[GC (Allocation Failure) [ParNew: 8803K->77K(9792K), 0.0006901 secs] 28953K->20227K(31680K), 0.0007201 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC (Allocation Failure) [ParNew: 8781K->117K(9792K), 0.0006956 secs] 28931K->20269K(31680K), 0.0007248 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC (Allocation Failure) [ParNew: 8821K->147K(9792K), 0.0007803 secs] 28973K->20302K(31680K), 0.0008104 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC (Allocation Failure) [ParNew: 8851K->144K(9792K), 0.0006512 secs] 29006K->20299K(31680K), 0.0006838 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC (Allocation Failure) [ParNew: 8848K->85K(9792K), 0.0006731 secs] 29003K->20244K(31680K), 0.0007214 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC (Allocation Failure) [ParNew: 8789K->51K(9792K), 0.0006959 secs] 28948K->20210K(31680K), 0.0007423 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[CMS-concurrent-mark: 0.026/0.041 secs] [Times: user=0.20 sys=0.01, real=0.04 secs] 
[CMS-concurrent-preclean-start]
[GC (Allocation Failure) [ParNew: 8755K->68K(9792K), 0.0006936 secs] 28914K->20228K(31680K), 0.0007320 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
[CMS-concurrent-preclean: 0.003/0.004 secs] [Times: user=0.02 sys=0.00, real=0.00 secs] 
[CMS-concurrent-abortable-preclean-start]
[CMS-concurrent-abortable-preclean: 0.001/0.001 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
[GC (CMS Final Remark) [YG occupancy: 8772 K (9792 K)][Rescan (parallel) , 0.0009351 secs][weak refs processing, 0.0000254 secs][class unloading, 0.0051529 secs][scrub symbol table, 0.0059575 secs][scrub string table, 0.0005636 secs][1 CMS-remark: 20160K(21888K)] 28932K(31680K), 0.0126847 secs] [Times: user=0.02 sys=0.00, real=0.02 secs] 
[CMS-concurrent-sweep-start]
[GC (Allocation Failure) [ParNew: 8772K->77K(9792K), 0.0006202 secs] 28932K->20237K(31680K), 0.0006609 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC (Allocation Failure) [ParNew: 8781K->83K(9792K), 0.0007143 secs] 28866K->20168K(31680K), 0.0007556 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC (Allocation Failure) [ParNew: 8787K->66K(9792K), 0.0007090 secs] 28843K->20125K(31680K), 0.0007489 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC (Allocation Failure) [ParNew: 8770K->67K(9792K), 0.0008660 secs] 28828K->20126K(31680K), 0.0008992 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
[GC (Allocation Failure) [ParNew: 8771K->64K(9792K), 0.0006686 secs] 28807K->20102K(31680K), 0.0006980 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
[CMS-concurrent-sweep: 0.011/0.016 secs] [Times: user=0.08 sys=0.00, real=0.01 secs] 
[CMS-concurrent-reset-start]
[CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
```

### G1 GC

> $ java -jar -Xmx64m -Xms64m -XX:+UseG1GC -XX:+PrintGC gateway-server-0.0.1-SNAPSHOT.jar
压测指令
```bash
wrk -c 40 -d30s http://localhost:8088/api/Hello
```
```log
Running 30s test @ http://localhost:8088/api/Hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    11.65ms   19.27ms 317.07ms   90.31%
    Req/Sec     3.73k     1.03k    5.37k    71.81%
  221448 requests in 30.01s, 56.85MB read
  Non-2xx or 3xx responses: 221448
Requests/sec:   7379.87
Transfer/sec:      1.89MB
```
```log
[GC pause (G1 Evacuation Pause) (young) 56482K->28868K(65536K), 0.0013769 secs]
[GC pause (G1 Evacuation Pause) (young) (initial-mark) 55492K->28856K(65536K), 0.0016727 secs]
[GC concurrent-root-region-scan-start]
[GC concurrent-root-region-scan-end, 0.0010540 secs]
[GC concurrent-mark-start]
[GC pause (G1 Evacuation Pause) (young) 55480K->28867K(65536K), 0.0011180 secs]
[GC pause (G1 Evacuation Pause) (young) 55491K->28842K(65536K), 0.0010825 secs]
[GC pause (G1 Evacuation Pause) (young) 55466K->28895K(65536K), 0.0009993 secs]
[GC concurrent-mark-end, 0.0291347 secs]
[GC remark, 0.0053880 secs]
[GC cleanup 36170K->36170K(65536K), 0.0003163 secs]
```

> $ java -jar -Xmx32m -Xms32m -XX:+UseG1GC -XX:+PrintGC gateway-server-0.0.1-SNAPSHOT.jar
压测指令
```bash
wrk -c 40 -d30s http://localhost:8088/api/Hello
```
```log
Running 30s test @ http://localhost:8088/api/Hello
  2 threads and 40 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    18.81ms   24.73ms 331.53ms   90.78%
    Req/Sec     1.53k   442.64     2.23k    75.00%
  90795 requests in 30.02s, 23.31MB read
  Non-2xx or 3xx responses: 90795
Requests/sec:   3024.95
Transfer/sec:    795.20KB
```
```log
[GC pause (G1 Evacuation Pause) (young) 26046K->21974K(32768K), 0.0008744 secs]
[GC concurrent-mark-end, 0.0399301 secs]
[GC remark, 0.0042096 secs]
[GC cleanup 24026K->24026K(32768K), 0.0002767 secs]
[GC pause (G1 Evacuation Pause) (young) 26070K->21932K(32768K), 0.0009041 secs]
[GC pause (G1 Evacuation Pause) (young) 26028K->21956K(32768K), 0.0010125 secs]
[GC pause (G1 Evacuation Pause) (young) (initial-mark) 26052K->21959K(32768K), 0.0012043 secs]
[GC concurrent-root-region-scan-start]
[GC concurrent-root-region-scan-end, 0.0010015 secs]
[GC concurrent-mark-start]
[GC pause (G1 Evacuation Pause) (young) 26055K->21991K(32768K), 0.0010464 secs]
[GC pause (G1 Evacuation Pause) (young) 26087K->21962K(32768K), 0.0008819 secs]
[GC pause (G1 Evacuation Pause) (young) 26058K->21965K(32768K), 0.0013550 secs]
[GC pause (G1 Evacuation Pause) (young) 26061K->22000K(32768K), 0.0010249 secs]
[GC pause (G1 Evacuation Pause) (young) 26096K->21990K(32768K), 0.0009415 secs]
[GC pause (G1 Evacuation Pause) (young) 26086K->21947K(32768K), 0.0023680 secs]
[GC pause (G1 Evacuation Pause) (young) 26043K->21937K(32768K), 0.0011027 secs]
[GC concurrent-mark-end, 0.0296307 secs]
[GC remark, 0.0085984 secs]
[GC cleanup 25833K->25833K(32768K), 0.0003493 secs]
```