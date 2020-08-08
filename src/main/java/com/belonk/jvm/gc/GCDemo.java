package com.belonk.jvm.gc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sun on 2018/4/20.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class GCDemo {
	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Static fields/constants/initializer
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */



	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Instance fields
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */

	static List<byte[]> objects = new ArrayList<>();

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Constructors
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */



	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Public Methods
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */

	/**
	 * Java7和Java8的JVM差异：
	 * Java8将Java7的永久带（Perm）改为了元空间（Metaspace），元空间不使用JVM内存而是使用物理内存。字符串常量池移到了堆内存。
	 * <p>
	 * 使用VM参数：-Xms2m -Xmx2m -XX:+PrintGCDetails，设置JVM堆内存为2M，并打印详细GC日志信息。
	 * <p>
	 * 运行一端时间过后，打印OOM信息：java.lang.OutOfMemoryError: Java heap space
	 * 堆内存溢出。
	 * <p>
	 * 通过PrintGCDetails参数，能够详细的跟踪GC信息，如下信息表明JVM的堆内存结构：
	 * 1、PSYoungGen：年轻代，包括三个部分
	 * （1）eden：伊甸园区
	 * （2）from：幸存者From区
	 * （3）to：幸存者to区
	 * 2、ParOldGen：老年代
	 * 3、Metaspace：元空间
	 */
	public static void main(String[] args) {
		// 持有一个100KB的数组
		byte[] buff = new byte[1024 * 100];
		while (true) {
			objects.add(buff);
		}
		/**
		 * [GC (Allocation Failure) [PSYoungGen: 512K->448K(1024K)] 512K->448K(1536K), 0.0006712 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
		 * [GC (Allocation Failure) [PSYoungGen: 958K->464K(1024K)] 958K->464K(1536K), 0.0011945 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
		 * [GC (Allocation Failure) [PSYoungGen: 975K->496K(1024K)] 975K->536K(1536K), 0.0013193 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
		 * [GC (Allocation Failure) [PSYoungGen: 945K->506K(1024K)] 985K->706K(1536K), 0.0010634 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
		 * [Full GC (Ergonomics) [PSYoungGen: 956K->417K(1024K)] [ParOldGen: 477K->431K(512K)] 1434K->848K(1536K), [Metaspace: 2982K->2982K(1056768K)], 0.0090238 secs] [Times: user=0.04 sys=0.00, real=0.01 secs]
		 * [Full GC (Ergonomics) [PSYoungGen: 837K->832K(1024K)] [ParOldGen: 431K->153K(512K)] 1269K->986K(1536K), [Metaspace: 2982K->2982K(1056768K)], 0.0128413 secs] [Times: user=0.07 sys=0.00, real=0.01 secs]
		 * [GC (Allocation Failure) --[PSYoungGen: 832K->832K(1024K)] 986K->994K(1536K), 0.0017681 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
		 * [Full GC (Allocation Failure) [PSYoungGen: 832K->416K(1024K)] [ParOldGen: 161K->143K(512K)] 994K->560K(1536K), [Metaspace: 2982K->2982K(1056768K)], 0.0037005 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]
		 * Heap
		 *  PSYoungGen      total 1024K, used 487K [0x00000007bfe80000, 0x00000007c0000000, 0x00000007c0000000)
		 *   eden space 512K, 95% used [0x00000007bfe80000,0x00000007bfef9ec8,0x00000007bff00000)
		 *   from space 512K, 0% used [0x00000007bff80000,0x00000007bff80000,0x00000007c0000000)
		 *   to   space 512K, 79% used [0x00000007bff00000,0x00000007bff66028,0x00000007bff80000)
		 *  ParOldGen       total 512K, used 143K [0x00000007bfe00000, 0x00000007bfe80000, 0x00000007bfe80000)
		 *   object space 512K, 28% used [0x00000007bfe00000,0x00000007bfe23e18,0x00000007bfe80000)
		 *  Metaspace       used 3066K, capacity 4496K, committed 4864K, reserved 1056768K
		 *   class space    used 332K, capacity 388K, committed 512K, reserved 1048576K
		 * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
		 * 	at java.util.Arrays.copyOf(Arrays.java:3210)
		 * 	at java.util.Arrays.copyOf(Arrays.java:3181)
		 * 	at java.util.ArrayList.grow(ArrayList.java:265)
		 * 	at java.util.ArrayList.ensureExplicitCapacity(ArrayList.java:239)
		 * 	at java.util.ArrayList.ensureCapacityInternal(ArrayList.java:231)
		 * 	at java.util.ArrayList.add(ArrayList.java:462)
		 * 	at com.belonk.jvm.OOMETest.main(OOMETest.java:59)
		 */
	}

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Private Methods
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */


}

