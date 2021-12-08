package com.belonk.io;

import java.io.File;
import java.util.Arrays;

/**
 * Created by sun on 2021/12/8.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class DirList {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		// 当前类所在的目录
		File dir = new File(DirList.class.getResource("").getPath());
		// 返回目录下的所有文件和目录
		String[] list = dir.list();
		// 排序
		Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
		for (String s : list) {
			System.out.println(s);
		}

		System.out.println();

		// 通过FilenameFilter过滤文件
		list = dir.list((dir1, name) -> name.endsWith(".class"));
		Arrays.sort(list, String.CASE_INSENSITIVE_ORDER);
		for (String s : list) {
			System.out.println(s);
		}
		/*
		DirList$1.class
		DirList.class`
		file
		image
		media
		nio

		DirList$1.class
		DirList.class`
		 */
	}
}