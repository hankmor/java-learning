package com.belonk.io.nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Created by sun on 2021/12/12.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ByteBufferToPrimitiveType {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * ByteBuffer与基础类型的存储和获取操作。
	 */

	public static void main(String[] args) {
		int size = 1024;
		ByteBuffer byteBuffer = ByteBuffer.allocate(size);
		// 检查是否默认的字节都是0
		while (byteBuffer.hasRemaining()) {
			if (byteBuffer.get() != 0) {
				System.out.println("none zero");
			}
		}
		System.out.println("limit: " + byteBuffer.limit());

		// 操作CharBuffer
		byteBuffer.rewind();
		CharBuffer charBuffer = byteBuffer.asCharBuffer().put("Hello Java");
		// 打印charBuffer
		charBuffer.flip();
		System.out.println("CharBuffer: " + charBuffer);
		// 打印byteBuffer
		System.out.print("ByteBuffer: ");
		while (byteBuffer.hasRemaining()) {
			char c = byteBuffer.getChar();
			if (c != 0) {
				System.out.print(c);
			} else {
				break;
			}
		}
		System.out.println();

		// 操作ShortBuffer
		byteBuffer.rewind();
		ShortBuffer shortBuffer = byteBuffer.asShortBuffer().put((short) 471142); // 转型后长度截取造成结果不同
		shortBuffer.flip();
		System.out.println("ShortBuffer: " + shortBuffer.get());
		System.out.println("ByteBuffer: " + byteBuffer.getShort());

		// 操作IntBuffer
		byteBuffer.rewind();

	}
}