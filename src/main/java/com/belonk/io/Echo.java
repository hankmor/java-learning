package com.belonk.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by sun on 2021/12/10.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class Echo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 标准输入流读取
	 */

	public static void main(String[] args) throws IOException {
		// 读取输入的内容
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		String line = bufferedReader.readLine();
		// 按行读取，如果没有任何输入，程序退出
		while (line != null && !"".equals(line)) {
			System.out.println(line);
			line = bufferedReader.readLine();
		}
	}
}