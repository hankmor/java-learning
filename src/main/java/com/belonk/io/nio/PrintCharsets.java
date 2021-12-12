package com.belonk.io.nio;

import java.nio.charset.Charset;
import java.util.SortedMap;

/**
 * Created by sun on 2021/12/12.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class PrintCharsets {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 打印所有可用的字符集
	 */

	public static void main(String[] args) {
		// 当前可用的字符集，key为字符集名称，value为字符集对象
		SortedMap<String, Charset> charsets = Charset.availableCharsets();
		for (String name : charsets.keySet()) {
			// 打印名称
			System.out.print(name + ": ");
			Charset charset = charsets.get(name);
			// 打印字符集的所有别名
			for (String alias : charset.aliases()) {
				System.out.print(alias + " ");
			}
			System.out.println();
		}
	}
}