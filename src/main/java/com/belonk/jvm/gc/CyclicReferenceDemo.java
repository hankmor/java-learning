package com.belonk.jvm.gc;

/**
 * Created by sun on 2020/8/8.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class CyclicReferenceDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		// 创建两个循环引用的对象
		Ref objectA = new Ref();
		Ref objectB = new Ref();
		objectA.ref = objectB;
		objectB.ref = objectA;

		// 对象实例设置为null，以便能够GC
		// objectA = null;
		// objectB = null;

		// 手动发起GC
		System.gc();
	}
}

class Ref {
	//这个成员属性唯一的作用就是占用10KB内存
	byte[] bigSize = new byte[1024 * 10];
	Object ref = null;
}