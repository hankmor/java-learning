package com.belonk.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by sun on 2021/12/8.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class UseRandomAccessFile {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws IOException {
		String file = "src/main/java/com/belonk/io/rdata.dt";
		UseRandomAccessFile randomAccessFile = new UseRandomAccessFile();
		randomAccessFile.write(new File(file));
	}

	static void write(File file) throws IOException {
		// 读写模式，写文件
		RandomAccessFile r = new RandomAccessFile(file, "rw");
		// 连续写7个double
		for (int i = 0; i < 7; i++) {
			r.writeDouble(i * 3.1415926d);
		}
		// 再写一个UTF字符串
		r.writeUTF("写完了");
		r.close();

		// 读取一次
		display(file);

		// 修改写入
		r = new RandomAccessFile(file, "rw");
		// 定位到第4个double，写入新数据,（修改）
		// 因为一个double占8个字节，所以字节位置应该是3 * 8
		r.seek(3 * 8);
		r.writeDouble(1.1234d);
		r.close();

		// 再次读取
		display(file);
	}

	static void display(File file) throws IOException {
		// 读取模式
		RandomAccessFile r = new RandomAccessFile(file, "r");
		for (int i = 0; i < 7; i++) {
			System.out.println(r.readDouble());
		}
		System.out.println(r.readUTF());
		r.close();
	}
}