## 题目
**2.（必做）**自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件。文件群里提供。

### 解题
继承ClassLoader类，重写findClass方法。
读取文件后，针对每个字节做x=255-x取模操作，得到正确的Java字节码的字节数组
调用definedClass方法，加载指定类，并返回Class类型
发射执行hello方法