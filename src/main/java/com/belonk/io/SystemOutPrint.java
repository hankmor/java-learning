package com.belonk.io;

import java.io.PrintWriter;

/**
 * Created by sun on 2021/12/10.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class SystemOutPrint {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 另一种System.out方式，使用PrintWriter包装
	 */

	public static void main(String[] args) {
		// System.out是一个OutputStream，用PrintWriter包装
		PrintWriter printWriter = new PrintWriter(System.out);
		printWriter.println("Hello, java!");
		printWriter.flush();

		// 第二个参数指的是自动刷新输出流缓冲区，就可以不需要调用flush()方法了
		printWriter = new PrintWriter(System.out, true);
		printWriter.println("你好，java!");
	}
}