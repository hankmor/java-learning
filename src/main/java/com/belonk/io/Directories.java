package com.belonk.io;

import java.io.File;

/**
 * Created by sun on 2021/12/8.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class Directories {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		// 当前目录
		String path = Directories.class.getResource("").getPath();
		File dir = new File(path);
		System.out.println("path: " + dir.getAbsolutePath());
		System.out.println("canRead: " + dir.canRead());
		System.out.println("canWrite: " + dir.canWrite());
		System.out.println("name: " + dir.getName());
		System.out.println("parent: " + dir.getParent());
		System.out.println("length: " + dir.length());
		System.out.println("lastModified: " + dir.lastModified());
		System.out.println("type: " + (dir.isDirectory() ? "directory" : "file"));
/*
path: /Users/sam/workspace/mine/java-learning/target/classes/com/belonk/io
canRead: true
canWrite: true
name: io
parent: /Users/sam/workspace/mine/java-learning/target/classes/com/belonk
length: 256
lastModified: 1638933853000
type: directory
 */
	}
}