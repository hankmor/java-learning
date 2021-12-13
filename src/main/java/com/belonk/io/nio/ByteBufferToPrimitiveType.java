package com.belonk.io.nio;

import java.nio.*;

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
	 *
	 * 使用ByteBuffer写入基本数据类型最简单快速的方法时使用ByteBuffer的视图。
	 *
	 * ByteBuffer提供了asCharBuffer()、asShortBuffer()、asIntBuffer()、asFloatBuffer()、asLongBuffer()、asDoubleBuffer()等方法，
	 * 他们都返回一个新的缓冲区视图，更改其内容会反映到ByteBuffer中，但是新的缓冲区视图的位置(position)、限制(limit)、标记(mark)都是独立的，
	 * 容量(capacity)与原ByteBuffer相同。
	 */

	public static void main(String[] args) {
		int size = 1024;
		ByteBuffer byteBuffer = ByteBuffer.allocate(size);
		// 检查是否默认的字节都是0(默认都是0)
		while (byteBuffer.hasRemaining()) {
			if (byteBuffer.get() != 0) {
				System.out.println("none zero");
			}
		}
		System.out.println("limit: " + byteBuffer.limit());
		System.out.println("capacity: " + byteBuffer.capacity());
		System.out.println("position: " + byteBuffer.position());

		// 存取CharBuffer
		byteBuffer.rewind();
		CharBuffer charBuffer = byteBuffer.asCharBuffer().put("Hello Java");
		// 打印charBuffer
		charBuffer.flip();
		System.out.println("Get from CharBuffer: " + charBuffer);
		// 打印byteBuffer
		System.out.print("Get from ByteBuffer: ");
		while (byteBuffer.hasRemaining()) {
			char c = byteBuffer.getChar();
			if (c != 0) {
				System.out.print(c);
			} else {
				break;
			}
		}
		System.out.println();

		// 存取ShortBuffer
		byteBuffer.rewind();
		ShortBuffer shortBuffer = byteBuffer.asShortBuffer().put((short) 471142); // 转型后长度截取造成结果不同
		shortBuffer.flip();
		System.out.println("Get from ShortBuffer: " + shortBuffer.get());
		System.out.println("Get from ByteBuffer: " + byteBuffer.getShort());

		// 存取IntBuffer
		byteBuffer.rewind();
		IntBuffer intBuffer = byteBuffer.asIntBuffer().put(99471142);
		intBuffer.flip();
		System.out.println("Get from IntBuffer: " + intBuffer.get());
		System.out.println("Get from ByteBuffer: " + byteBuffer.getInt());

		// 存取LongBuffer
		byteBuffer.rewind();
		LongBuffer longBuffer = byteBuffer.asLongBuffer().put(99471142);
		longBuffer.flip();
		System.out.println("Get from LongBuffer: " + longBuffer.get());
		System.out.println("Get from ByteBuffer: " + byteBuffer.getLong());

		// 存取FloatBuffer
		byteBuffer.rewind();
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer().put(99471142);
		floatBuffer.flip();
		System.out.println("Get from FloatBuffer: " + floatBuffer.get());
		System.out.println("Get from ByteBuffer: " + byteBuffer.getFloat());

		// 存储DoubleBuffer
		byteBuffer.rewind();
		DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer().put(99471142);
		doubleBuffer.flip();
		System.out.println("Get from DoubleBuffer: " + doubleBuffer.get());
		System.out.println("Get from ByteBuffer: " + byteBuffer.getDouble());

		/*
		limit: 1024
		capacity: 1024
		position: 1024
		Get from CharBuffer: Hello Java
		Get from ByteBuffer: Hello Java
		Get from ShortBuffer: 12390
		Get from ByteBuffer: 12390
		Get from IntBuffer: 99471142
		Get from ByteBuffer: 99471142
		Get from LongBuffer: 99471142
		Get from ByteBuffer: 99471142
		Get from FloatBuffer: 9.9471144E7
		Get from ByteBuffer: 9.9471144E7
		Get from DoubleBuffer: 9.9471142E7
		Get from ByteBuffer: 9.9471142E7
		 */
	}
}