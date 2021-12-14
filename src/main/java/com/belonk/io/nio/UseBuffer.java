package com.belonk.io.nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Created by sun on 2021/12/14.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class UseBuffer {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 交换每两个字符串的位置
	 */

	public static void main(String[] args) {
		String str = "HelloWorld";
		ByteBuffer byteBuffer = ByteBuffer.allocate(str.length() * 2);
		CharBuffer charBuffer = byteBuffer.asCharBuffer();
		charBuffer.put(str);
		System.out.println(charBuffer.rewind());

		// 交换两两char的位置
		UseBuffer.symmetricScramble(charBuffer);
		System.out.println(charBuffer.rewind());

		UseBuffer.symmetricScramble(charBuffer);
		System.out.println(charBuffer.rewind());

		/*
		HelloWorld
		eHllWorodl
		HelloWorld
		 */
	}

	static void symmetricScramble(CharBuffer charBuffer) {
		while (charBuffer.hasRemaining()) {
			// 标记：mark设置为当前position
			charBuffer.mark();
			char c1 = charBuffer.get();
			char c2 = charBuffer.get();
			// 重置：reset方法将position设置为mark
			charBuffer.reset();
			// 交换位置
			charBuffer.put(c2);
			charBuffer.put(c1);
		}
	}
}