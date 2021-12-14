package com.belonk.io.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/12/14.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class LargeMappedFiles {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 内存映射文件：允许创建、修改太大而不能放入内存的文件，提高性能。底层是通过操作系统的文件映射工具来最大的提高性能。
	 * 内存映射文件，指的是操作系统不能一次读取大文件到内存，此时可以通过将文件与内存进行逻辑关联，将部分文件映射到内存，其他部分交换到文件系统，
	 * 以实现对整个文件快速的读取和修改。
	 *
	 * MappedByteBuffer：直接字节缓冲区，其内容是文件的内存映射区域。
	 * 映射字节缓冲区是通过FileChannel.map方法创建的。 此类使用特定于内存映射文件区域的操作扩展了ByteBuffer类。
	 * 映射的字节缓冲区及其表示的文件映射在缓冲区本身被垃圾收集之前一直有效。
	 * 映射字节缓冲区的内容可以随时更改，例如，如果该程序或其他程序更改了映射文件的相应区域的内容。 此类更改是否发生以及何时发生取决于操作系统，因此未指定。
	 * 映射字节缓冲区的全部或部分可能在任何时候变得不可访问，例如，如果映射文件被截断。 尝试访问映射字节缓冲区的不可访问区域不会更改缓冲区的内容，并且会导致在访问时或稍后的某个时间抛出未指定的异常。 因此，强烈建议采取适当的预防措施，以避免该程序或同时运行的程序对映射文件的操作，但读取或写入文件的内容除外。
	 * 映射字节缓冲区的其他行为与普通的直接字节缓冲区没有什么不同。
	 */

	public static void main(String[] args) throws IOException, InterruptedException {
		int length = 0x8FFFFFF; // 约为140M的大小
		String file = "src/main/java/com/belonk/io/nio/largeFile.dat";
		// 创建可读可写大文件
		FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel();
		// 创建内存映射，可以指定内存映射的区域，即表示可以映射部分文件
		MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, length);
		// 写数据
		for (int i = 0; i < length; i++) {
			mappedByteBuffer.put((byte) 'a');
		}
		fileChannel.close();
		System.out.println("finished writing.");
		// 部分读取
		for (int i = length / 2; i < length / 2 + 5; i++) {
			System.out.println((char) mappedByteBuffer.get(i));
		}

		// TODO 删不掉
		System.out.println("deleting file");
		new File(file).delete();
	}
}