package com.belonk.io.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by sun on 2021/12/11.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ChannelCopy {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 使用FileChannel拷贝文件
	 */

	public static void main(String[] args) throws IOException {
		int size = 1024;
		String srcFile = "src/main/java/com/belonk/io/nio/ChannelCopy.java";
		String destFile = "src/main/java/com/belonk/io/nio/ChannelCopy.txt";
		// 获得输入输出文件的Channel
		FileChannel in = new FileInputStream(srcFile).getChannel();
		FileChannel out = new FileOutputStream(destFile).getChannel();

		// 缓冲区分配大小
		ByteBuffer byteBuffer = ByteBuffer.allocate(size);
		// 读取到缓冲区
		while (in.read(byteBuffer) != -1) {
			// 保证缓冲区可以被out提取
			byteBuffer.flip();
			// 将缓冲区的数据写入到out中
			out.write(byteBuffer);
			// 清空缓冲区，以便下次继续读取
			byteBuffer.clear();
		}

		// 可以使用更简单的transferTo或者transferFrom方法来实现，下边两行效果完全相同，返回转换的字节大小
		// long transfer = in.transferTo(0, in.size(), out);
		// long transfer = out.transferFrom(in, 0, in.size());
		// System.out.println(transfer);
	}
}