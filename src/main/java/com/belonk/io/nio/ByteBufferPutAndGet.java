package com.belonk.io.nio;

import java.nio.ByteBuffer;
import java.time.Year;
import java.util.Arrays;

/**
 * Created by sun on 2021/12/13.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ByteBufferPutAndGet {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * ByteBuffer put过后值的变化
	 */

	static int size = 5;
	static ByteBuffer byteBuffer = ByteBuffer.allocate(size);

	public static void main(String[] args) {
		// put值
		byteBuffer.put(new byte[]{1, 2});
		// position放到开头，以便从头读取
		byteBuffer.rewind();
		print(); // 1 2 0 0 0

		// 继续在位置2写入，position设置为2
		byteBuffer.position(2);
		byteBuffer.put((byte) 3);
		byteBuffer.rewind();
		print(); // 1 2 3 0 0

		// position改为0，更改头元素
		byteBuffer.position(0);
		byteBuffer.put((byte) 4);
		byteBuffer.rewind();
		print(); // 4 2 3 0 0

		// position改为2，更改其元素
		byteBuffer.position(2);
		byteBuffer.put((byte) 5);
		byteBuffer.rewind();
		print(); // 4 2 5 0 0

		// 继续填充最后两个元素
		byteBuffer.position(3);
		byteBuffer.put((byte) 6);
		byteBuffer.put((byte) 7);
		byteBuffer.rewind();
		print(); // 4 2 5 6 7

		/*
		1 2 0 0 0
		1 2 3 0 0
		4 2 3 0 0
		4 2 5 0 0
		4 2 5 6 7
		*/
	}

	static void print() {
		while (byteBuffer.hasRemaining()) {
			System.out.print(byteBuffer.get() + " ");
		}
		System.out.println();
	}
}