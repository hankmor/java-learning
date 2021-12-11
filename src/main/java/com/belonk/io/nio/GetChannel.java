package com.belonk.io.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * Created by sun on 2021/12/11.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class GetChannel {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 演示通过java的nio的FileChannel来读写文件
	 *
	 * 老io中，三个类被修改了用来生成FileChannel，分别是：FileOuputStream、FileInputStream和RandomAccessFile，Reader和Writer不能
	 * 产生通道，但是可以通过java.nio.channels.Channels的方法在通道中产生Reader和Writer。
	 */

	public static void main(String[] args) throws IOException {
		String filePath = "src/main/java/com/belonk/io/nio/data.txt";
		// 写入文件
		FileChannel fileChannel = new FileOutputStream(filePath).getChannel();
		fileChannel.write(ByteBuffer.wrap("write something\n".getBytes(StandardCharsets.UTF_8)));
		fileChannel.close();

		// 在文件最后追加
		fileChannel = new RandomAccessFile(filePath, "rw").getChannel();
		// 定位到文件末尾，fileChannel.size()返回通道中文件的大小
		fileChannel.position(fileChannel.size());
		fileChannel.write(ByteBuffer.wrap("write another something\n".getBytes(StandardCharsets.UTF_8)));
		fileChannel.write(ByteBuffer.wrap("再写一些中文".getBytes(StandardCharsets.UTF_8)));
		fileChannel.close();

		// 读取文件
		// 分配缓冲区大小
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		fileChannel = new FileInputStream(filePath).getChannel();
		// 通道读取文件到缓冲区
		fileChannel.read(byteBuffer);
		// 调用filp()方法表示缓冲区可以读取了
		byteBuffer.flip();
		// 循环读取
		while (byteBuffer.hasRemaining()) {
			// 读取每个字节，转为char输出，这样中文肯定读取有问题（后续编码来解决）
			System.out.print((char) byteBuffer.get());
		}
		/*
		write something
		write another something
		￥ﾆﾍ￥ﾆﾙ￤ﾸﾀ￤ﾺﾛ￤ﾸﾭ￦ﾖﾇ
		 */
	}
}