package com.belonk.concurrent.ff;

/**
 * Created by sun on 2021/4/7.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class FinalFieldRefDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		for (int i = 0; i < 1000000; i++) {
			new Thread(FinalRefCls::read).start(); // 线程3
			new Thread(FinalRefCls::write1).start(); // 线程1
			new Thread(FinalRefCls::write2).start(); // 线程2
		}
		/*
		FinalRefCls中：
		1、1和3，2和3都不能重排序
		2、步骤2的结果finalRef[0]值为1，所有线程都能看到，但是步骤4的结果（finalRef[0]值为2）读线程可能看不到，即：finalRef[0]的值可能为1也可能为2，但不会是0
		3、步骤5写final引用类型值，步骤7读final引用类型值，但是读写成可能看不到，可能读到的是初始值0
		 */
	}
}

class FinalRefCls {
	final int[] finalRef; // final引用类型域，在构造函数赋值
	static FinalRefCls obj;

	FinalRefCls() { // 构造函数
		finalRef = new int[2]; // 1 写final域
		finalRef[0] = 1; // 2 写final域引用对象的成员域
	}

	public static void write1() {
		obj = new FinalRefCls(); // 3 写对象引用
	}

	public static void write2() {
		obj.finalRef[0] = 2; // 4 写final域引用对象的成员域
		obj.finalRef[1] = 10; // 5 写final域引用对象的成员域
	}

	public static void read() {
		if (obj != null) {
			int x = obj.finalRef[0]; // 6 读final域引用对象的成员域
			int y = obj.finalRef[1]; // 7 读final域引用对象的成员域
			// System.out.println(Thread.currentThread().getName() + ": x = " + x + ", y = " + y);
			if (x == 0) {
				// 不可能打印如下信息, 遵守final域重排序规则
				System.err.println("final禁止重排序失败，final引用类型的成员域读取错误");
			}
			if (x != 2) {
				// 可能输出如下信息，因为读线程可能没有看到写线程写的值
				System.err.println("读final引用类型的成员域[0]出错: " + x);
			}
			if (y != 10) {
				// 可能输出如下信息，因为读线程可能没有看到写线程写的值
				System.err.println("读final引用类型成员域[1]出错: " + y);
			}
		}
	}
}