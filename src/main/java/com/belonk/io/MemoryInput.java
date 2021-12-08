package com.belonk.io;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by sun on 2021/12/8.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class MemoryInput {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 将读取到内存的文本文件内容，按照字符读取并输出
	 */

	public static void main(String[] args) throws IOException {
		String file = "src/main/java/com/belonk/io/MemoryInput.java";
		String content = BufferedInputFile.read(file);
		// StringReader替换了早期的 StringBufferInputStream，用来读取字符串
		StringReader stringReader = new StringReader(content);
		int c;
		// 按照字符逐个读取，结果输出为char
		while ((c = stringReader.read()) != -1) {
			System.out.print((char) c);
		}
		stringReader.close();
	}
}