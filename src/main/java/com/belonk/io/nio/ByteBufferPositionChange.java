package com.belonk.io.nio;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by sun on 2021/12/13.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ByteBufferPositionChange {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 使用Buffer的rewind、flip、clear等方法更改内部position、limit、mark。
	 */

	public static void main(String[] args) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(10);
		print(byteBuffer);// 10, 10, 0

		byteBuffer.put(new byte[]{1, 2});
		print(byteBuffer);// 10, 10, 2

		byteBuffer.rewind();
		print(byteBuffer); // 10, 10, 0

		byteBuffer.put((byte) -1);
		print(byteBuffer); // 10, 10, 1

		byteBuffer.flip();
		print(byteBuffer); // 10, 1, 0

		byteBuffer.clear();
		print(byteBuffer); // 10, 10, 0
		/*
		capacity, limit, position: 10, 10, 0
		capacity, limit, position: 10, 10, 2
		capacity, limit, position: 10, 10, 0
		capacity, limit, position: 10, 10, 1
		capacity, limit, position: 10, 1, 0
		capacity, limit, position: 10, 10, 0
		 */
	}

	static void print(ByteBuffer byteBuffer) {
		System.out.printf("capacity, limit, position: %d, %d, %d", byteBuffer.capacity(), byteBuffer.limit(), byteBuffer.position());
		System.out.println();
	}
}