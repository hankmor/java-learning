package com.belonk.io.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * Created by sun on 2021/12/11.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class BufferToText {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * ByteBuffer 转为文本中的编码解码问题
	 */

	public static void main(String[] args) throws IOException {
		String filePath = "src/main/java/com/belonk/io/nio/data.txt";
		FileChannel out = new FileOutputStream(filePath).getChannel();
		out.write(ByteBuffer.wrap("写中文".getBytes(StandardCharsets.UTF_8)));
		out.close();

		int size = 1024;
		FileChannel in = new FileInputStream(filePath).getChannel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(size);
		in.read(byteBuffer);
		in.close();
		// 保证bytebuffer可以读取了
		byteBuffer.flip();
		// 中文乱码
		System.out.println(byteBuffer.asCharBuffer());
		// 缓冲区重置到开头
		byteBuffer.rewind();
		// 使用UTF-8编码来解码
		CharBuffer decoded = StandardCharsets.UTF_8.decode(byteBuffer);
		// 现在ok了
		System.out.println(decoded);

		// 再次写数据
		String content = "再写了中文";
		out = new FileOutputStream(filePath).getChannel();
		// 重新创建ByteBuffer
		byteBuffer = ByteBuffer.allocate(content.getBytes(StandardCharsets.UTF_8).length);
		// 通过CharBuffer来写数据
		byteBuffer.asCharBuffer().put(content);
		out.write(byteBuffer);
		out.close();
		// 再次读取
		in = new FileInputStream(filePath).getChannel();
		in.read(byteBuffer);
		in.close();
		byteBuffer.flip();
		System.out.println(byteBuffer.asCharBuffer());
	}

	/*
	 * 駤뢭
	 * 写中文
	 * 再写了中文
	 */
}