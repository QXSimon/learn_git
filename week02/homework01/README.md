##　homework01

**1.（选做）**使用 GCLogAnalysis.java 自己演练一遍串行 / 并行 /CMS/G1 的案例。


java -XX:+PrintGCDetails GCLogAnalysis   

java -XX:+PrintGCDetails -Xmx1g -Xms1g GCLogAnalysis   
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xmx1g -Xms1g GCLogAnalysis
//打印日志到文件,则控制台不会输出信息
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xloggc:gc.demo.log -Xmx1g -Xms1g GCLogAnalysis

Allocation Failure 内存分配失败
PCYoungGC, Full GC,

### 分析
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xmx512m -Xms512m GCLogAnalysis

YoungGC: 2021-07-03T13:09:45.426+0800: 0.101: [GC (Allocation Failure) [PSYoungGen: 131584K->21502K(153088K)] 131584K->40958K(502784K), 0.0055120 secs] [Times: user=0.02 sys=0.03, real=0.01 secs]

2021-07-03T13:09:45.426+0800: 0.101: 发生GC时间
[GC (Allocation Failure):　因内存分配失败发生GC
[PSYoungGen:                Young的区GC
131584K->21502K(153088K)]:　Young 区内存总共 153088K, GC 前使用了131584K内存, GC后压缩到了21502K:
131584K->40958K(502784K) : 堆内存冲共502784K, 发生GC前堆内存使用131584K, GC后被压缩到40958K
, 0.0055120 secs] [Times: user=0.02 sys=0.03, real=0.01 secs]: 此次GC,总用时０.0055120 秒

Full GC
2021-07-03T13:09:45.651+0800: 0.326: [Full GC (Ergonomics) [PSYoungGen: 22566K->0K(116736K)] [ParOldGen: 321933K->247421K(349696K)] 344500K->247421K(466432K), [Metaspace: 2720K->2720K(1056768K)], 0.0416356 secs] [Times: user=0.22 sys=0.01, real=0.04 secs]
Meta区(持久代): [Metaspace: 2720K->2720K(1056768K)]

Young GC 一般指Young区GC, 有时也叫Minor GC, 小型GC 
Full GC 指OLd 区加上Young区的GC, 有时也叫Major GC, 大型GC

####　-XX:+UseSerialGC 串行化GC
DefNew,也是Young区的GC ,只不名字不一样而已.
java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xmx1g -Xms1g -XX:+UseSerialGC GCLogAnalysis

年轻代GC 
2021-07-03T21:50:52.558+0800: [GC (Allocation Failure) 2021-07-03T21:50:52.558+0800: [DefNew: 314560K->34942K(314560K), 0.0372303 secs] 849479K->653970K(1013632K), 0.0372667 secs] [Times: user=0.01 sys=0.02, real=0.03 secs] 

年轻代GC 并且 老年代GC
2021-07-03T21:50:52.627+0800: [GC (Allocation Failure) 2021-07-03T21:50:52.627+0800: [DefNew: 314558K->314558K(314560K), 0.0000160 secs]2021-07-03T21:50:52.627+0800: [Tenured: 619027K->379654K(699072K), 0.0458442 secs] 933586K->379654K(1013632K), [Metaspace: 2720K->2720K(1056768K)], 0.0459183 secs] [Times: user=0.05 sys=0.00, real=0.04 secs] 

####　并行化GC, -XX:UseParallelGC, JDK8 默认就是并行GC 
> $ java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xmx1g -Xms1g -XX:+UseParallelGC GCLogAnalysis                                                                                                                                                            [±main ●●]

堆内存增加后,比如从-Xmx1g -> -Xmx4g, 结果会GC的频率会降低,很久才会执行一次,同时单次GC的时间变长.因为内存变大了嘛.

#### 在不配置Xms的情况下,观察Full GC 与Young GC, 还有内存分配

因为不配置Xms寻,堆内存初始值比较小,所以GC的频率提高,Full GC变多
多几次GC后,堆内存慢慢提升到最大值Xmx


#### CMS GC, -XX:+UseConcMarkSweepGC 
> $ java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xmx1g -Xms1g -XX:+UseConcMarkSweepGC GCLogAnalysis                                                                                                                                                       [±main ●●]

年轻代GC 
2021-07-03T22:16:03.212+0800: [GC (Allocation Failure) 2021-07-03T22:16:03.212+0800: [ParNew: 314558K->34944K(314560K), 0.0364094 secs] 604525K->400962K(1013632K), 0.0364566 secs] [Times: user=0.25 sys=0.03, real=0.04 secs]
CMS 初始化标记, Old区使用内存366018K,Old区容量699072K,整个堆内存目前使用401162K,容量1013632K
2021-07-03T22:16:03.248+0800: [GC (CMS Initial Mark) [1 CMS-initial-mark: 366018K(699072K)] 401162K(1013632K), 0.0003540 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
并发标记开始
2021-07-03T22:16:03.249+0800: [CMS-concurrent-mark-start]
2021-07-03T22:16:03.252+0800: [CMS-concurrent-mark: 0.003/0.003 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
并发预清理
2021-07-03T22:16:03.252+0800: [CMS-concurrent-preclean-start]
2021-07-03T22:16:03.252+0800: [CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
可取消的并发预清理开始
2021-07-03T22:16:03.252+0800: [CMS-concurrent-abortable-preclean-start]
在多个过程中,可能会多次执行ParNew年轻代GC 
2021-07-03T22:16:03.281+0800: [GC (Allocation Failure) 2021-07-03T22:16:03.281+0800: [ParNew: 314560K->34944K(314560K), 0.0392244 secs] 680578K->480242K(1013632K), 0.0392592 secs] [Times: user=0.27 sys=0.02, real=0.04 secs]
2021-07-03T22:16:03.353+0800: [GC (Allocation Failure) 2021-07-03T22:16:03.353+0800: [ParNew: 314560K->34943K(314560K), 0.0397082 secs] 759858K->562403K(1013632K), 0.0397455 secs] [Times: user=0.26 sys=0.04, real=0.04 secs]
2021-07-03T22:16:03.423+0800: [GC (Allocation Failure) 2021-07-03T22:16:03.423+0800: [ParNew: 314559K->34943K(314560K), 0.0443169 secs] 842019K->645512K(1013632K), 0.0443546 secs] [Times: user=0.28 sys=0.04, real=0.05 secs]
2021-07-03T22:16:03.498+0800: [GC (Allocation Failure) 2021-07-03T22:16:03.498+0800: [ParNew: 314559K->34942K(314560K), 0.0400503 secs] 925128K->726255K(1013632K), 0.0400982 secs] [Times: user=0.28 sys=0.01, real=0.04 secs]
可取消的并发预清理执行完
2021-07-03T22:16:03.538+0800: [CMS-concurrent-abortable-preclean: 0.006/0.285 secs] [Times: user=1.21 sys=0.11, real=0.29 secs]
CMS 最终标记
2021-07-03T22:16:03.538+0800: [GC (CMS Final Remark) [YG occupancy: 40692 K (314560 K)]2021-07-03T22:16:03.538+0800: [Rescan (parallel) , 0.0005195 secs]2021-07-03T22:16:03.538+0800: [weak refs processing, 0.0000208 secs]2021-07-03T22:16:03.538+0800: [class unloading, 0.0001914 secs]2021-07-03T22:16:03.539+0800: [scrub symbol table, 0.0002960 secs]2021-07-03T22:16:03.539+0800: [scrub string table, 0.0001531 secs][1 CMS-remark: 691313K(699072K)] 732005K(1013632K), 0.0012344 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
并发清理开始
2021-07-03T22:16:03.539+0800: [CMS-concurrent-sweep-start]
并发清理执行结束
2021-07-03T22:16:03.540+0800: [CMS-concurrent-sweep: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
并发重置开始
2021-07-03T22:16:03.540+0800: [CMS-concurrent-reset-start]
并发重置执行结束
2021-07-03T22:16:03.543+0800: [CMS-concurrent-reset: 0.002/0.002 secs] [Times: user=0.00 sys=0.01, real=0.00 secs] 

#### CMS 提升内存到4g
发生GC数变少,而且只有ParNew GC

#### G1 GC, -XX:+UseG1GC
$ java -XX:+PrintGCDetails -XX:+PrintGCDateStamps -Xmx4g -Xms4g -XX:+UseG1GC GCLogAnalysis
正在运行....

Young GC
2021-07-03T22:36:01.452+0800: [GC pause (G1 Evacuation Pause) (young), 0.0026314 secs]
[Parallel Time: 1.8 ms, GC Workers: 8]
[GC Worker Start (ms): Min: 74.5, Avg: 74.6, Max: 74.7, Diff: 0.2]
[Ext Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 0.9]
[Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
[Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]
[Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
[Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
[Object Copy (ms): Min: 1.3, Avg: 1.3, Max: 1.5, Diff: 0.2, Sum: 10.7]
[Termination (ms): Min: 0.0, Avg: 0.2, Max: 0.2, Diff: 0.2, Sum: 1.5]
[Termination Attempts: Min: 1, Avg: 1.0, Max: 1, Diff: 0, Sum: 8]
[GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
[GC Worker Total (ms): Min: 1.6, Avg: 1.7, Max: 1.7, Diff: 0.2, Sum: 13.3]
[GC Worker End (ms): Min: 76.2, Avg: 76.2, Max: 76.3, Diff: 0.0]
[Code Root Fixup: 0.0 ms]
[Code Root Purge: 0.0 ms]
[Clear CT: 0.2 ms]
[Other: 0.6 ms]
[Choose CSet: 0.0 ms]
[Ref Proc: 0.2 ms]
[Ref Enq: 0.0 ms]
[Redirty Cards: 0.1 ms]
[Humongous Register: 0.0 ms]
[Humongous Reclaim: 0.0 ms]
[Free CSet: 0.0 ms]
[Eden: 52224.0K(52224.0K)->0.0B(45056.0K) Survivors: 0.0B->7168.0K Heap: 67363.4K(1024.0M)->21931.1K(1024.0M)]
[Times: user=0.01 sys=0.00, real=0.00 secs]

混合GC
2021-07-03T22:36:01.873+0800: [GC pause (G1 Evacuation Pause) (mixed), 0.0083074 secs]
[Parallel Time: 5.7 ms, GC Workers: 8]
[GC Worker Start (ms): Min: 495.2, Avg: 495.2, Max: 495.3, Diff: 0.1]
[Ext Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.1, Sum: 0.7]
[Update RS (ms): Min: 0.1, Avg: 0.2, Max: 0.7, Diff: 0.6, Sum: 1.6]
[Processed Buffers: Min: 0, Avg: 3.1, Max: 4, Diff: 4, Sum: 25]
[Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.1, Diff: 0.0, Sum: 0.2]
[Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
[Object Copy (ms): Min: 4.4, Avg: 4.9, Max: 5.1, Diff: 0.7, Sum: 39.3]
[Termination (ms): Min: 0.0, Avg: 0.1, Max: 0.2, Diff: 0.2, Sum: 0.9]
[Termination Attempts: Min: 1, Avg: 1.2, Max: 2, Diff: 1, Sum: 10]
[GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
[GC Worker Total (ms): Min: 5.3, Avg: 5.3, Max: 5.4, Diff: 0.1, Sum: 42.8]
[GC Worker End (ms): Min: 500.6, Avg: 500.6, Max: 500.6, Diff: 0.1]
[Code Root Fixup: 0.0 ms]
[Code Root Purge: 0.0 ms]
[Clear CT: 0.3 ms]
[Other: 2.3 ms]
[Choose CSet: 0.1 ms]
[Ref Proc: 1.5 ms]
[Ref Enq: 0.0 ms]
[Redirty Cards: 0.5 ms]
[Humongous Register: 0.1 ms]
[Humongous Reclaim: 0.0 ms]
[Free CSet: 0.1 ms]
[Eden: 21504.0K(21504.0K)->0.0B(172.0M) Survivors: 30720.0K->5120.0K Heap: 741.8M(1024.0M)->643.6M(1024.0M)]
[Times: user=0.04 sys=0.02, real=0.01 secs]
2021-07-03T22:36:02.389+0800: [GC concurrent-root-region-scan-start]
2021-07-03T22:36:02.389+0800: [GC concurrent-root-region-scan-end, 0.0001508 secs]
2021-07-03T22:36:02.389+0800: [GC concurrent-mark-start]
2021-07-03T22:36:02.391+0800: [GC concurrent-mark-end, 0.0019984 secs]
2021-07-03T22:36:02.391+0800: [GC remark 2021-07-03T22:36:02.391+0800: [Finalize Marking, 0.0001963 secs] 2021-07-03T22:36:02.391+0800: [GC ref-proc, 0.0001794 secs] 2021-07-03T22:36:02.391+0800: [Unloading, 0.0004327 secs], 0.0016803 secs]
[Times: user=0.01 sys=0.00, real=0.00 secs]
2021-07-03T22:36:02.393+0800: [GC cleanup 527M->527M(1024M), 0.0006546 secs]
[Times: user=0.00 sys=0.00, real=0.00 secs]
执行结束!共生成对象次数:17778
Heap
garbage-first heap   total 1048576K, used 771099K [0x00000000c0000000, 0x00000000c0102000, 0x0000000100000000)
region size 1024K, 198 young (202752K), 7 survivors (7168K)
Metaspace       used 2727K, capacity 4486K, committed 4864K, reserved 1056768K
class space    used 286K, capacity 386K, committed 512K, reserved 1048576K

####　太复杂了,使用PrintGC,代替 PrintGCDetails, 这样精简输出

正在运行....
最开始G1的暂停, Young GC
2021-07-03T22:41:06.041+0800: [GC pause (G1 Evacuation Pause) (young) 64753K->25154K(1024M), 0.0038106 secs]
2021-07-03T22:41:06.055+0800: [GC pause (G1 Evacuation Pause) (young) 78182K->40927K(1024M), 0.0040279 secs]
2021-07-03T22:41:06.067+0800: [GC pause (G1 Evacuation Pause) (young) 96342K->57812K(1024M), 0.0039612 secs]
2021-07-03T22:41:06.082+0800: [GC pause (G1 Evacuation Pause) (young) 125M->80371K(1024M), 0.0052043 secs]
2021-07-03T22:41:06.129+0800: [GC pause (G1 Evacuation Pause) (young) 237M->123M(1024M), 0.0091728 secs]
2021-07-03T22:41:06.156+0800: [GC pause (G1 Evacuation Pause) (young) 245M->159M(1024M), 0.0109852 secs]
2021-07-03T22:41:06.195+0800: [GC pause (G1 Evacuation Pause) (young) 330M->209M(1024M), 0.0068369 secs]
2021-07-03T22:41:06.233+0800: [GC pause (G1 Evacuation Pause) (young) 410M->267M(1024M), 0.0081730 secs]
2021-07-03T22:41:06.324+0800: [GC pause (G1 Evacuation Pause) (young) 637M->359M(1024M), 0.0119636 secs]
大对象的内存分配失败,　导致使用了Ｇ１的初始化标记, 步骤和CMS 很相似
2021-07-03T22:41:06.368+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 644M->415M(1024M), 0.0122414 secs]
并发根区域扫描开始
2021-07-03T22:41:06.381+0800: [GC concurrent-root-region-scan-start]
2021-07-03T22:41:06.381+0800: [GC concurrent-root-region-scan-end, 0.0002182 secs]
并发标记开始
2021-07-03T22:41:06.381+0800: [GC concurrent-mark-start]
2021-07-03T22:41:06.385+0800: [GC concurrent-mark-end, 0.0042163 secs]
重新地标记
2021-07-03T22:41:06.385+0800: [GC remark, 0.0017516 secs]
并行清理
2021-07-03T22:41:06.387+0800: [GC cleanup 450M->435M(1024M), 0.0007035 secs]
并发清理开始
2021-07-03T22:41:06.388+0800: [GC concurrent-cleanup-start]
2021-07-03T22:41:06.388+0800: [GC concurrent-cleanup-end, 0.0000211 secs]

2021-07-03T22:41:06.449+0800: [GC pause (G1 Evacuation Pause) (young)-- 831M->564M(1024M), 0.0127507 secs]
混合GC 
2021-07-03T22:41:06.463+0800: [GC pause (G1 Evacuation Pause) (mixed) 571M->484M(1024M), 0.0094187 secs]
2021-07-03T22:41:06.472+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 484M->483M(1024M), 0.0022017 secs]
2021-07-03T22:41:06.475+0800: [GC concurrent-root-region-scan-start]
2021-07-03T22:41:06.475+0800: [GC concurrent-root-region-scan-end, 0.0001102 secs]
2021-07-03T22:41:06.475+0800: [GC concurrent-mark-start]
2021-07-03T22:41:06.477+0800: [GC concurrent-mark-end, 0.0020821 secs]
2021-07-03T22:41:06.477+0800: [GC remark, 0.0018561 secs]
2021-07-03T22:41:06.479+0800: [GC cleanup 501M->497M(1024M), 0.0010051 secs]
2021-07-03T22:41:06.480+0800: [GC concurrent-cleanup-start]
2021-07-03T22:41:06.480+0800: [GC concurrent-cleanup-end, 0.0000149 secs]
2021-07-03T22:41:06.521+0800: [GC pause (G1 Evacuation Pause) (young) 850M->562M(1024M), 0.0111586 secs]
2021-07-03T22:41:06.535+0800: [GC pause (G1 Evacuation Pause) (mixed) 579M->475M(1024M), 0.0073058 secs]
2021-07-03T22:41:06.548+0800: [GC pause (G1 Evacuation Pause) (mixed) 530M->443M(1024M), 0.0054724 secs]
2021-07-03T22:41:06.554+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 445M->443M(1024M), 0.0028380 secs]
2021-07-03T22:41:06.557+0800: [GC concurrent-root-region-scan-start]
2021-07-03T22:41:06.557+0800: [GC concurrent-root-region-scan-end, 0.0001357 secs]
2021-07-03T22:41:06.557+0800: [GC concurrent-mark-start]
2021-07-03T22:41:06.559+0800: [GC concurrent-mark-end, 0.0013930 secs]
2021-07-03T22:41:06.559+0800: [GC remark, 0.0019453 secs]
2021-07-03T22:41:06.561+0800: [GC cleanup 458M->451M(1024M), 0.0013831 secs]
2021-07-03T22:41:06.562+0800: [GC concurrent-cleanup-start]
2021-07-03T22:41:06.562+0800: [GC concurrent-cleanup-end, 0.0000185 secs]
2021-07-03T22:41:06.608+0800: [GC pause (G1 Evacuation Pause) (young)-- 850M->611M(1024M), 0.0095886 secs]
2021-07-03T22:41:06.619+0800: [GC pause (G1 Evacuation Pause) (mixed) 623M->536M(1024M), 0.0124329 secs]
2021-07-03T22:41:06.637+0800: [GC pause (G1 Evacuation Pause) (mixed) 594M->550M(1024M), 0.0039846 secs]
2021-07-03T22:41:06.642+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 553M->551M(1024M), 0.0024041 secs]
2021-07-03T22:41:06.644+0800: [GC concurrent-root-region-scan-start]
2021-07-03T22:41:06.644+0800: [GC concurrent-root-region-scan-end, 0.0001591 secs]
2021-07-03T22:41:06.644+0800: [GC concurrent-mark-start]
2021-07-03T22:41:06.646+0800: [GC concurrent-mark-end, 0.0018949 secs]
2021-07-03T22:41:06.646+0800: [GC remark, 0.0019269 secs]
2021-07-03T22:41:06.648+0800: [GC cleanup 569M->564M(1024M), 0.0006303 secs]
2021-07-03T22:41:06.649+0800: [GC concurrent-cleanup-start]
2021-07-03T22:41:06.649+0800: [GC concurrent-cleanup-end, 0.0000187 secs]
2021-07-03T22:41:06.682+0800: [GC pause (G1 Evacuation Pause) (young) 849M->611M(1024M), 0.0091826 secs]
2021-07-03T22:41:06.694+0800: [GC pause (G1 Evacuation Pause) (mixed) 635M->525M(1024M), 0.0061152 secs]
2021-07-03T22:41:06.707+0800: [GC pause (G1 Evacuation Pause) (mixed) 586M->475M(1024M), 0.0075494 secs]
2021-07-03T22:41:06.715+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 476M->475M(1024M), 0.0015815 secs]
2021-07-03T22:41:06.717+0800: [GC concurrent-root-region-scan-start]
2021-07-03T22:41:06.717+0800: [GC concurrent-root-region-scan-end, 0.0001318 secs]
2021-07-03T22:41:06.717+0800: [GC concurrent-mark-start]
2021-07-03T22:41:06.718+0800: [GC concurrent-mark-end, 0.0016145 secs]
2021-07-03T22:41:06.718+0800: [GC remark, 0.0015762 secs]
2021-07-03T22:41:06.720+0800: [GC cleanup 487M->486M(1024M), 0.0006122 secs]
2021-07-03T22:41:06.721+0800: [GC concurrent-cleanup-start]
2021-07-03T22:41:06.721+0800: [GC concurrent-cleanup-end, 0.0000184 secs]
2021-07-03T22:41:06.760+0800: [GC pause (G1 Evacuation Pause) (young)-- 845M->620M(1024M), 0.0098953 secs]
2021-07-03T22:41:06.772+0800: [GC pause (G1 Evacuation Pause) (mixed) 635M->554M(1024M), 0.0095541 secs]
2021-07-03T22:41:06.790+0800: [GC pause (G1 Evacuation Pause) (mixed) 615M->563M(1024M), 0.0048608 secs]
2021-07-03T22:41:06.795+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 564M->562M(1024M), 0.0022065 secs]
2021-07-03T22:41:06.798+0800: [GC concurrent-root-region-scan-start]
2021-07-03T22:41:06.798+0800: [GC concurrent-root-region-scan-end, 0.0001093 secs]
2021-07-03T22:41:06.798+0800: [GC concurrent-mark-start]
2021-07-03T22:41:06.800+0800: [GC concurrent-mark-end, 0.0018725 secs]
2021-07-03T22:41:06.800+0800: [GC remark, 0.0020057 secs]
2021-07-03T22:41:06.802+0800: [GC cleanup 582M->575M(1024M), 0.0006602 secs]
2021-07-03T22:41:06.803+0800: [GC concurrent-cleanup-start]
2021-07-03T22:41:06.803+0800: [GC concurrent-cleanup-end, 0.0000183 secs]
2021-07-03T22:41:06.832+0800: [GC pause (G1 Evacuation Pause) (young) 823M->618M(1024M), 0.0093043 secs]
2021-07-03T22:41:06.844+0800: [GC pause (G1 Evacuation Pause) (mixed) 649M->538M(1024M), 0.0072773 secs]
2021-07-03T22:41:06.857+0800: [GC pause (G1 Evacuation Pause) (mixed) 589M->483M(1024M), 0.0079206 secs]
2021-07-03T22:41:06.866+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 485M->484M(1024M), 0.0062303 secs]
2021-07-03T22:41:06.872+0800: [GC concurrent-root-region-scan-start]
2021-07-03T22:41:06.872+0800: [GC concurrent-root-region-scan-end, 0.0001406 secs]
2021-07-03T22:41:06.872+0800: [GC concurrent-mark-start]
2021-07-03T22:41:06.874+0800: [GC concurrent-mark-end, 0.0015609 secs]
2021-07-03T22:41:06.874+0800: [GC remark, 0.0018639 secs]
2021-07-03T22:41:06.876+0800: [GC cleanup 498M->496M(1024M), 0.0010006 secs]
2021-07-03T22:41:06.877+0800: [GC concurrent-cleanup-start]
2021-07-03T22:41:06.877+0800: [GC concurrent-cleanup-end, 0.0000190 secs]
2021-07-03T22:41:06.916+0800: [GC pause (G1 Evacuation Pause) (young)-- 855M->651M(1024M), 0.0091529 secs]
2021-07-03T22:41:06.927+0800: [GC pause (G1 Evacuation Pause) (mixed) 668M->582M(1024M), 0.0102430 secs]
2021-07-03T22:41:06.944+0800: [GC pause (G1 Evacuation Pause) (mixed) 652M->603M(1024M), 0.0043951 secs]
2021-07-03T22:41:06.949+0800: [GC pause (G1 Humongous Allocation) (young) (initial-mark) 611M->607M(1024M), 0.0025225 secs]
2021-07-03T22:41:06.952+0800: [GC concurrent-root-region-scan-start]
2021-07-03T22:41:06.952+0800: [GC concurrent-root-region-scan-end, 0.0000934 secs]
2021-07-03T22:41:06.952+0800: [GC concurrent-mark-start]
2021-07-03T22:41:06.954+0800: [GC concurrent-mark-end, 0.0019062 secs]
2021-07-03T22:41:06.954+0800: [GC remark, 0.0014878 secs]
2021-07-03T22:41:06.956+0800: [GC cleanup 620M->608M(1024M), 0.0005538 secs]
2021-07-03T22:41:06.956+0800: [GC concurrent-cleanup-start]
2021-07-03T22:41:06.956+0800: [GC concurrent-cleanup-end, 0.0000207 secs]
2021-07-03T22:41:06.983+0800: [GC pause (G1 Evacuation Pause) (young) 844M->654M(1024M), 0.0089922 secs]
2021-07-03T22:41:06.996+0800: [GC pause (G1 Evacuation Pause) (mixed) 682M->567M(1024M), 0.0063480 secs]
2021-07-03T22:41:07.008+0800: [GC pause (G1 Evacuation Pause) (mixed) 620M->508M(1024M), 0.0076046 secs]

调整内存到256m后,出现了Full GC,并OOM
Full GC
2021-07-03T22:49:16.571+0800: [Full GC (Allocation Failure)  204M->251K(256M), 0.0017376 secs]
2021-07-03T22:49:16.572+0800: [GC concurrent-mark-abort]
OOM 
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
at GCLogAnalysis.generateGarbage(GCLogAnalysis.java:47)
at GCLogAnalysis.main(GCLogAnalysis.java:27)


执行结束!共生成对象次数:17746

升级到4g后 ,只发生了YoungGC, GC频率降低
使用G1 GC时,一定要防止Full GC, G1GC退化成串行化GC 

#### 可视化GC 日志工具
1.GCEasy
在线的,gceasy.io
把GC 日志复制上去就可以了

2.GCViewer
是一个jar包,展示的信息没有GCEasy详细和好看

#### 各种GC 有什么特点和使用场景?
自己多弄些场景,把日志看习惯,都会了.
与别人沟通和自己分析时,就妻得心应手.