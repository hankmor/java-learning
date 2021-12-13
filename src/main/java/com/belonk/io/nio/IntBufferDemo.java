package com.belonk.io.nio;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by sun on 2021/12/13.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class IntBufferDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 使用IntBuffer操作数据
	 */

	public static void main(String[] args) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		// 创建IntBuffer视图，具有单独的position、limit、mark等，需改会反映到原byteBuffer中
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		// 存入数据
		intBuffer.put(new int[]{1, 2, 3, 4, 5});
		// 第4个位置上的值
		System.out.println(intBuffer.get(3));
		// 修改第4个位置上的值
		intBuffer.put(3, 100);
		System.out.println(intBuffer.get(3));
		// 设置limit为当前位置，然后position置为0并清空标记。这样就可以只读取写过数据的字节
		intBuffer.flip();
		while (intBuffer.hasRemaining()) {
			System.out.print(intBuffer.get() + " ");
		}
		/*
		4
		100
		1 2 3 100 5
		 */
	}
}