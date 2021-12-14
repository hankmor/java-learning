package com.belonk.io.nio;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by sun on 2021/12/14.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class Endians {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 字节存放序列demo。
	 *
	 * ByteBuffer有两中字节存放序列：BIG_ENDIAN(低位优先)、LITTLE_ENDIAN(高位优先)。
	 *
	 * 默认情况下，ByteBuffer的字节存放序列为BIG_ENDIAN（低位优先），可以使用order()方法更改存放序列模式
	 */

	public static void main(String[] args) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[10]);

		// 默认字节序列是BIG_ENDIAN
		byteBuffer.asCharBuffer().put("abcde");
		System.out.println(Arrays.toString(byteBuffer.array()));

		// 设置字节序列
		byteBuffer.rewind();
		byteBuffer.order(ByteOrder.BIG_ENDIAN); // 设置字节序列
		byteBuffer.asCharBuffer().put("abcde"); // 填充数据
		System.out.println(Arrays.toString(byteBuffer.array()));

		// 更改字节序列
		byteBuffer.rewind();
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		byteBuffer.asCharBuffer().put("abcde");
		System.out.println(Arrays.toString(byteBuffer.array()));

		/*
		[0, 97, 0, 98, 0, 99, 0, 100, 0, 101]
		[0, 97, 0, 98, 0, 99, 0, 100, 0, 101]
		[97, 0, 98, 0, 99, 0, 100, 0, 101, 0]
		*/

		// 使用两个字节的ByteBuffer存储byte，但是字节序列不同，按照两个字节的short读取，结果不同

		ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[2]);
		byteBuffer1.put(new byte[]{0, 97}); // 写入两个字节的数据
		byteBuffer1.flip(); // 重置position
		System.out.println(Arrays.toString(byteBuffer1.array()));
		System.out.println(byteBuffer1.asShortBuffer().get());

		// 更改字节序列重新写数据并读取
		byteBuffer1.clear();
		byteBuffer1.order(ByteOrder.LITTLE_ENDIAN);
		byteBuffer1.put(new byte[]{0, 97});
		byteBuffer1.flip(); // 重置position
		System.out.println(Arrays.toString(byteBuffer1.array()));
		System.out.println(byteBuffer1.asShortBuffer().get());

		/*
		[0, 97]
		97
		[0, 97]
		24832
		 */
	}
}