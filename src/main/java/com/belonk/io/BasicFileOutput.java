package com.belonk.io;

import java.io.*;

/**
 * Created by sun on 2021/12/8.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class BasicFileOutput {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws IOException {
		String outFile = "src/main/java/com/belonk/io/BasicFileOutput.out";
		String inFile = "src/main/java/com/belonk/io/BasicFileOutput.java";
		BufferedReader bufferedReader = new BufferedReader(new FileReader(inFile));
		// 使用PrintWriter写文本文件，用BufferedWriter、FileWriter包装
		// PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(outFile)));
		// 上边一行的包装可以简化为如下代码，效果一样
		PrintWriter printWriter = new PrintWriter(outFile);
		String line;
		int lineCnt = 0; // 自定义行号
		while ((line = bufferedReader.readLine()) != null) {
			printWriter.write((++lineCnt) + ": " + line + "\n");
		}
		bufferedReader.close();
		printWriter.close();
		// 再次读取文本文件
		System.out.println(BufferedInputFile.read(outFile));
		File file = new File(outFile);
		// 删除文件
		if (file.exists())
			file.delete();
	}
}