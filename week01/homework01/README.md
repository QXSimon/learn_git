## 题目
**1.（选做）**自己写一个简单的 Hello.java，里面需要涉及基本类型，四则运行，if 和 for，然后自己分析一下对应的字节码，有问题群里讨论。

### 1.run commands 
```bash
# complie
javac -g src/Hello.java
# get a list of instructions
javap -c -v src/Hello
```

### 3.Analyze bytecodes
```
Classfile /home/simonqiu/GitRepositories/learn_git/week01/homework01/src/Hello.class
  Last modified 2021-6-27; size 1030 bytes	//最后编辑时间；文件大小
  MD5 checksum 7c07aa5bc5190fd4f58d8181dcb0b622	 //MD5信息
  Compiled from "Hello.java"	//从Hello.java 编译过来的
public class Hello	
  minor version: 0	//小版本号
  major version: 52	//大版本号， 合起来就是52.0版本号，这代表jdk8
  flags: ACC_PUBLIC, ACC_SUPER	//修饰符，public, 它继承自Object
//常量池
Constant pool:	//常量池
	//方法引用， 由常量12的对象调用常量52方法
   #1 = Methodref          #12.#52        // java/lang/Object."<init>":()V
   #2 = Integer            100000	//Integer类型值
   #3 = Long               985211996000l	//Long类型值
   #5 = Float              999.99f			//Float类型值
   #6 = Double             3.14159268210382d	//Double类型值
   //属性引用
   #8 = Fieldref           #53.#54        // java/lang/System.out:Ljava/io/PrintStream;
   //String类型值，指向55号常量，也就是“Hello bytecode"
   #9 = String             #55            // Hello bytecode
  #10 = Methodref          #56.#57        // java/io/PrintStream.println:(Ljava/lang/String;)V
   //类或接口的符号引用
  #11 = Class              #58            // Hello
  #12 = Class              #59            // java/lang/Object
   //UTF8编码的字符串字面值
  #13 = Utf8               <init>		//初始化方法
  #14 = Utf8               ()V			//无参数方法，放回类型void
  #15 = Utf8               Code			
  #16 = Utf8               LineNumberTable
  #17 = Utf8               LocalVariableTable
  #18 = Utf8               this			//对象this自身引用
  #19 = Utf8               LHello;
  #20 = Utf8               main
  #21 = Utf8               ([Ljava/lang/String;)V
  #22 = Utf8               i
  #23 = Utf8               I
  #24 = Utf8               arr
  #25 = Utf8               [I
  #26 = Utf8               args
  #27 = Utf8               [Ljava/lang/String;	//String数组
  #28 = Utf8               a
  #29 = Utf8               B
  #30 = Utf8               b
  #31 = Utf8               S
  #32 = Utf8               c
  #33 = Utf8               d
  #34 = Utf8               J
  #35 = Utf8               e
  #36 = Utf8               F
  #37 = Utf8               f
  #38 = Utf8               D
  #39 = Utf8               g
  #40 = Utf8               Z
  #41 = Utf8               h
  #42 = Utf8               C
  #43 = Utf8               add
  #44 = Utf8               subtract
  #45 = Utf8               multiply
  #46 = Utf8               divide
  #47 = Utf8               StackMapTable
  #48 = Class              #27            // "[Ljava/lang/String;"
  #49 = Class              #25            // "[I"
  #50 = Utf8               SourceFile
  #51 = Utf8               Hello.java
   //字段或方法的部分符号引用
  #52 = NameAndType        #13:#14        // "<init>":()V
  #53 = Class              #60            // java/lang/System
  #54 = NameAndType        #61:#62        // out:Ljava/io/PrintStream;
  #55 = Utf8               Hello bytecode
  #56 = Class              #63            // java/io/PrintStream
  #57 = NameAndType        #64:#65        // println:(Ljava/lang/String;)V
  #58 = Utf8               Hello
  #59 = Utf8               java/lang/Object
  #60 = Utf8               java/lang/System
  #61 = Utf8               out
  #62 = Utf8               Ljava/io/PrintStream;
  #63 = Utf8               java/io/PrintStream
  #64 = Utf8               println
  #65 = Utf8               (Ljava/lang/String;)V	
{
  public Hello();	//无参数函数
    descriptor: ()V //void, 返回类型
    flags: ACC_PUBLIC	//public
    Code:
      stack=1, locals=1, args_size=1	//执行本节代码需要使用栈深度1,本地变量1，参数1（this)
         0: aload_0	//偏移量0：把本地方法表里第0号槽位的引用类型变量加载到栈上来，a表示对象类型
         //偏移量1：调用函数，使用1号常量。映射过来就是Object的初始化方法。java/lang/Object.<init>:()V
         1: invokespecial #1      // Method java/lang/Object."<init>":()V   
         4: return	//偏移量4：返回符. 同时可以知道，1号偏移量的操作占用了3个字节：1、2、3
      LineNumberTable:	//行号表
        line 1: 0		//第0号偏移量的指令，出现在代码的第1行
      LocalVariableTable:	//本地方法表
        Start  Length  Slot  Name   Signature
            0       5     0  this   LHello;	//0号位是this对象本身的引用

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V	//方法参数是String[]数组类型，方法返回void
    flags: ACC_PUBLIC, ACC_STATIC	//方法修饰 public, static
    Code:
      stack=4, locals=17, args_size=1	//执行本节代码需要用到栈深度4,本地变量17,参数1
         //byte,short,boolean,都会被转换成int来操作
         //"istore_2"是压缩过的 "istore  2"
         0: bipush        69	//把byte常量值9推进操作数栈
         2: istore_1			//把栈顶int类型值存入局部变量1,也就是变革量a
         3: sipush        1013	//把short常量值013推进操作数栈
         6: istore_2			//把栈顶的int类型值存入局部变量2,
         7: ldc           #2                  // int 100000	//把常量池中#2号常量推进栈
         9: istore_3			//把栈顶int类型值存入3号局部变量
        10: ldc2_w        #3                  // long 985211996000l	//把常量池中long类型的#3号常量取出推到栈顶
        13: lstore        4		//把栈顶long类型值存入4号局部变量
        15: ldc           #5                  // float 999.99f	//把常量池#5号推进栈
        17: fstore        6		//把栈顶float类型值存入6号局部变量
        19: ldc2_w        #6                  // double 3.14159268210382d //把double类型的#6号常量取出推到栈顶
        22: dstore        7		//把栈顶double类型值存入7号局部变量
        24: iconst_0			//把int类型的常量0，推入栈
        25: istore        9		//把栈顶int类型值存入9号局部变量
        27: sipush        153	//把short常量值153推进栈
        30: istore        10	//栈顶的short转换成int，并存入10号变量
        32: sipush        187	//short常量值187入栈
        35: istore        11	//187出栈存入11号本地变量
        37: iconst_1			//int常量值1入栈
        38: istore        12	//1出栈，存入12号局部变量
        40: bipush        20	//byte常量20入栈
        42: istore        13	//20出栈，存入13号局部变量
        44: bipush        11	//。。。。。
        46: istore        14
        //17行代码,常量比较if判断，直接被省略了。
        48: iconst_4			//int常量值4入栈
        49: newarray       int	//new一个int数组，
        51: dup					//复制栈顶值，并入栈
        52: iconst_0			//常量值0入栈
        53: iload         11	//11号局部变量入栈
        55: iastore				//栈顶int值，存入数组0索引位置
        56: dup					//复制栈顶，并入栈
        57: iconst_1			//int常量值1入栈
        58: iload         12	//12号局部变量入栈
        60: iastore				//栈顶int值存入数组索引1位置， 这里的索引位置，就是iconst_1常量值，也就是1
        61: dup					//复制栈顶
        62: iconst_2			//int常量值2入栈
        63: iload         13	//13号局部变量入栈
        65: iastore				//int类型栈顶值存入索引2位置
        66: dup					//....
        67: iconst_3
        68: iload         14
        70: iastore
        71: astore        15	//栈顶引用类型值存入15号局部变量，也就是arr
        73: iconst_0			//常量0入栈
        74: istore        16	//存入16号本地变量
        76: iload         16	//16号本地变量入栈
        78: aload         15	//15号引用类型本地变量入栈
        80: arraylength			//数组长度值入栈
        81: if_icmpge     102	//比较栈顶两个值，0>=arr.length，如果符合，就跳转到102偏移量操作处
        84: aload         15	//15号引用类型本地变量入栈
        86: iload         16	//16号int类型本地变量入栈
        88: aload         15
        90: iload         16		
        92: iaload				//int类型数组0号索引位置值入栈
        93: iconst_1			//常量1入栈
        94: ishl				//把int值向左移动指定数，将结果入栈
        95: iastore				//把结果存入数组指定索引位置
        96: iinc          16, 1	//16号变量值自增1
        99: goto          76	//跳转到偏移量76的操作处， 这里表示的是，一次循环结束，回去下一轮循环的操作
        //获取常量#8的类静态域，并入栈。这里就是System.out，它是Ljava/io/PrintStream类型
       102: getstatic     #8                  // Field java/lang/System.out:Ljava/io/PrintStream;
        //#9号常量入栈
       105: ldc           #9                  // String Hello bytecode
       	//调用对象的实例方法，这里就是System.out对象的println方法，方法参数String, 返回类型void
       107: invokevirtual #10                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
       110: return		//返回符，方法结束
      LineNumberTable:	//行号对照表
        line 4: 0		//0号偏离量的操作在代码的第4行
        line 5: 3
        line 6: 7
        line 7: 10
        line 8: 15
        line 9: 19
        line 10: 24
        line 11: 27
        line 12: 32
        line 13: 37
        line 14: 40
        line 15: 44
        //16行，17行空格和无意义的if比较，被编译后，直接去除了
        line 18: 48	
        line 19: 73
        line 20: 84
        line 19: 96
        line 24: 102
        line 25: 110
      LocalVariableTable:	//局部变量表
        Start  Length  Slot  Name   Signature
           76      26    16     i   I	//16号变量，int类型
           73      29    15   arr   [I		//15号变量，int数组
            0     111     0  args   [Ljava/lang/String; //0号变量，String数组
            3     108     1     a   B	//1号变量，byte类型
            7     104     2     b   S	//short类型
           10     101     3     c   I	
           15      96     4     d   J	//long 类型
           19      92     6     e   F
           24      87     7     f   D
           27      84     9     g   Z	//boolean类型
           32      79    10     h   C	//char类型
           37      74    11   add   I
           40      71    12 subtract   I
           44      67    13 multiply   I
           48      63    14 divide   I
      StackMapTable: number_of_entries = 2 	//栈图
        frame_type = 255 /* full_frame */
          offset_delta = 76		//偏移量
          locals = [ class "[Ljava/lang/String;", int, int, int, long, float, double, int, int, int, int, int, int, class "[I", int ]	//本地类型
          stack = []
        frame_type = 249 /* chop */
          offset_delta = 25
}
SourceFile: "Hello.java"

```