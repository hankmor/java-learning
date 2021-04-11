package com.belonk.jvm;

/**
 * Created by sun on 2020/8/9.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class StackDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields

	double d = 0d;

	//~ Constructors


	//~ Methods

	/**
	 * Java栈：存放的是8中基本数据类型的变量、对象引用地址、方法。
	 * 栈中的数据类型：本地变量、栈操作、栈桢数据。
	 * <p>
	 * 参数设置：-Xms1m -Xmx1m -Xss256k
	 * <p>
	 * -Xss: 配置java栈内存大小
	 * <p>
	 * 栈内存设置太小问题：The stack size specified is too small, Specify at least 160k
	 */
	public static void main(String[] args) {
		// test1();
		test2();
	}

	public static void test1() {
		StackTest stackTest = new StackTest();
		stackTest.f1();
	}

	public static void test2() {
		new InnerClass();
	}

	static class InnerClass {
		// 非静态实例，递归创建，造成栈内存溢出
		// 引用类型，栈中存放的对象的引用地址
		InnerClass ic = new InnerClass();
	}
}

class StackTest {
	double d;

	// Stack Frame：栈桢，每一个方法调用都会创建一个栈桢，顶层栈桢即是当前正在执行方法，该方法调用其他方法则会再创建一个栈桢，
	// 压入栈顶，当前方法执行完成，栈桢出栈
	// 递归调用，导致栈溢出
	void f1() {
		f2();
	}

	/**
	 * 递归调用，导致栈内存不足，抛出如下异常：
	 * Exception in thread "main" java.lang.StackOverflowError
	 */
	void f2() {
		d += Math.E * Math.PI;
		f2();
	}
}