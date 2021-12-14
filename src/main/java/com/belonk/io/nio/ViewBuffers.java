package com.belonk.io.nio;

import java.nio.*;

/**
 * Created by sun on 2021/12/14.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ViewBuffers {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 通过ByteBuffer上的不同缓冲器，将同一个8字节序列转换为 char、short、int、long、float、double
	 */

	public static void main(String[] args) {
		// 创建填充了8个字节的ByteBuffer，第8个字节数据为a
		ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{0, 0, 0, 0, 0, 0, 0, 'a'});
		// 打印内容
		System.out.print("ByteBuffer: ");
		while (byteBuffer.hasRemaining()) {
			System.out.print(byteBuffer.position() + " -> " + byteBuffer.get() + ", ");
		}
		System.out.println();

		// 使用CharBuffer缓冲器视图
		CharBuffer charBuffer = ((ByteBuffer) byteBuffer.rewind()).asCharBuffer();
		System.out.print("CharBuffer: ");
		// java中一个char占2个字节，所以最终输出4个元素
		while (charBuffer.hasRemaining()) {
			System.out.print(charBuffer.position() + " -> " + charBuffer.get() + ", ");
		}
		System.out.println();

		// 使用ShortBuffer缓冲器
		ShortBuffer shortBuffer = ((ByteBuffer) byteBuffer.rewind()).asShortBuffer();
		System.out.print("ShortBuffer: ");
		// 一个short占2个字节，结果输出4个元素
		while (shortBuffer.hasRemaining()) {
			System.out.print(shortBuffer.position() + " -> " + shortBuffer.get() + ", ");
		}
		System.out.println();

		// 使用IntBuffer缓冲器
		IntBuffer intBuffer = ((ByteBuffer) byteBuffer.rewind()).asIntBuffer();
		System.out.print("IntBuffer: ");
		// 一个int占4个字节，结果输出2个元素
		while (intBuffer.hasRemaining()) {
			System.out.print(intBuffer.position() + " -> " + intBuffer.get() + ", ");
		}
		System.out.println();

		// 使用LongBuffer缓冲器
		LongBuffer longBuffer = ((ByteBuffer) byteBuffer.rewind()).asLongBuffer();
		System.out.print("LongBuffer: ");
		// 一个long占8个字节，结果输出一个元素
		while (longBuffer.hasRemaining()) {
			System.out.print(longBuffer.position() + " -> " + longBuffer.get() + ", ");
		}
		System.out.println();

		// 使用FloatBuffer缓冲器
		FloatBuffer floatBuffer = ((ByteBuffer) byteBuffer.rewind()).asFloatBuffer();
		System.out.print("FloatBuffer: ");
		// 一个float占4个字节，结果输出2个元素
		while (floatBuffer.hasRemaining()) {
			System.out.print(floatBuffer.position() + " -> " + floatBuffer.get() + ", ");
		}
		System.out.println();

		// 使用DoubleBuffer缓冲器
		DoubleBuffer doubleBuffer = ((ByteBuffer) byteBuffer.rewind()).asDoubleBuffer();
		System.out.print("DoubleBuffer: ");
		// 一个double占8个字节，结果输出一个元素
		while (doubleBuffer.hasRemaining()) {
			System.out.print(doubleBuffer.position() + " -> " + doubleBuffer.get() + ", ");
		}

		/*
		ByteBuffer: 0 -> 0, 1 -> 0, 2 -> 0, 3 -> 0, 4 -> 0, 5 -> 0, 6 -> 0, 7 -> 97,
		CharBuffer: 0 -> , 1 -> , 2 -> , 3 -> a,
		ShortBuffer: 0 -> 0, 1 -> 0, 2 -> 0, 3 -> 97,
		IntBuffer: 0 -> 0, 1 -> 97,
		LongBuffer: 0 -> 97,
		FloatBuffer: 0 -> 0.0, 1 -> 1.36E-43,
		DoubleBuffer: 0 -> 4.8E-322,
		 */
	}
}