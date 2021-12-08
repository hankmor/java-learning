Java io 系统主要有两组抽象类：`InputStream` 和`OutputStream`、`Reader` 和 `Writer`。

# InputStream 和 OutputStream

* `InputStream`：输入流，表示从不同的数据源（字节数组、文件、网络数据、String、管道、其他流序列等）中获取数据，包含基本的 `read()` 方法
* `OutputStream`：输出流，表述将流数据输出，包含基本的 `write()` 方法

Java io 采用装饰器模式设计，InputStream 下的装饰器基类为 FilterInputStream，OuputStream 下的装饰器基类为
FilterOutputStream，通过对不同的输出输出流进行装饰，可以组合不同的功能。

InputStream 的类型包括：

* ByteArrayInputStream：允许将内存缓冲区作为 InputStream
* StringBufferInputStream（废弃）：将String转为InputStream，被 StringReader代替
* FileInputStream：从文件读取信息
* PipedInputStream：产生用于写入 PipedOuputStream的数据，实现管道化
* SequenceInputStream：将多个InputStream合并为一个
* FilterInputStream：装饰器基类，组合不同的装饰器实现各种功能

InputStream 的装饰器 FilterInputStream 包括的类型有：

* DataInputStream：搭配 DataOutputStream 实现读取基本数据类型(int、char、long、double、float等)
* BufferedInputStream：增加缓冲区，避免每次读取都进行写操作，提高性能
* LineNumberInputStream：跟踪输入流的行号，不常用
* PushbackInputStream：可以将读取的最后一个字符回退，用于编译期的扫描器，开发者用不到

OutputStream 的类型包括：

* ByteArrayOutputStream：在内存中创建缓冲区，并写入数据
* FileOutputStream：将文件信息写入文件
* PipedOutputStream：任何写入其中的信息都会作为PipedInputStream的输出，实现管道化
* FilterOutputStream：装饰器

OutputStream 的类型包括：

* DataOutputStream：搭配 DataInputStream 实现写入基本数据类型(int、char、long、double、float等)
* PrintStream：用于生成格式化输出（DataOutputStream 处理数据存储，PrintStream 显示）
* BufferedOutputStream：增加缓冲区，避免每次读取数据都进行写操作

# Reader 和 Writer

Java 1.1新增，两者配合用来实现字符流的读取和写入，实现国际化。InputStreamReader 可以将 InputStream 转为 Reader，OutputStream 可以将 OutputStream 转为 Writer。

_io 对象在1.0和1.1的对应关系_

|原有|对应
|---|---
|InputStream                        | Reader，用 InputStreamReader 适配
|OuputStream                        | Writer，用 OutputStream 适配
|FileInputStream                    | FileReader
|FileOutStream                      | FileWriter
|~~StringbufferInputStream~~（废弃） | StringReader
|无相应的类               | StringWriter
|ByteArrayInputStream    | CharArrayReader
|ByteArrayOutputStream   | CharArrayWriter
|PipedInputStream        | PipedReader
|PipedOutputStream       | PipedWriter

_过滤器在1.0和1.1的对应关系_

|1.0的过滤器| 1.1的过滤器
|---|---
|FilterInputStream     | FilterReader
|FilterOutputStream    | FilterWriter (无子类)
|BufferedInputStream   | BufferedReader
|BufferedOutputStream  | BufferedWriter
|DataInputStream       | 按行读取使用BufferedReader，其他都应该使用DataInputStream
|PrintStream           | PrintWriter
|~~LineNumberInputStream~~ (废弃) | LineNumberReader
|StreamTokenizer       | StreamTokenizer（使用接受Reader的构造器）
|PushbackInputStream   | PushbackReader