package com.belonk.io;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by sun on 2021/12/8.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class FormattedMemoryInput {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * DataInputStream读取格式化内容，可以是任何字节流或者字符
	 */

	public static void main(String[] args) throws IOException {
		String file = "src/main/java/com/belonk/io/FormattedMemoryInput.java";
		// DataInputStream 读取面向字节的数据，而不是字符，读取基本数据类型
		DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(BufferedInputFile.read(file).getBytes(StandardCharsets.UTF_8)));
		// 没有可读取字节了，说明读取完成
		// 挨个读取字节，很明显中文读取有问题（2到3个字节）
		while (dataInputStream.available() != 0) {
			int read = dataInputStream.readByte();
			System.out.print((char) read);
		}
	}
}