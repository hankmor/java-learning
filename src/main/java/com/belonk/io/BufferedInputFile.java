package com.belonk.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by sun on 2021/12/8.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class BufferedInputFile {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 从文本文件中按行读取内容
	 */

	public static void main(String[] args) throws IOException {
		// 相对于工程根目录
		String file = "src/main/java/com/belonk/io/BufferedInputFile.java";
		System.out.println(read(file));
	}

	public static String read(String file) throws IOException {
		StringBuilder builder = new StringBuilder();
		// 读文本文件
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		String line;
		// 按行读取，readLine方法会去掉换行符，因此需要加上
		while ((line = bufferedReader.readLine()) != null) {
			builder.append(line).append("\n");
		}
		// 关闭读取器
		bufferedReader.close();
		return builder.toString();
	}
}