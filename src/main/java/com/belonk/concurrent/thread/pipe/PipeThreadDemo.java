package com.belonk.concurrent.thread.pipe;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * Created by sun on 2021/4/16.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class PipeThreadDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws IOException {
		/*
		线程间交换数据的管道流：
		1、PipedWriter、PipedReader 交换字符
		2、PipedOutputStream、PipedInputStream 交换字节
		 */
		PipedWriter writer = new PipedWriter();
		PipedReader reader = new PipedReader();
		// 管道必须连接起来，才能交换数据
		writer.connect(reader);
		// 创建一个线程来读取另一个线程的输入内容
		new Thread(new MyPipedReader(reader), "read-trhead").start();
		// 创建一个线程写入控制太输入的内容到管道流中
		new Thread(new MyPipedWriter(writer), "write-start").start();
	}
}

class MyPipedReader implements Runnable {
	PipedReader reader;

	public MyPipedReader(PipedReader reader) {
		this.reader = reader;
	}

	@Override
	public void run() {
		try {
			read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void read() throws IOException {
		int i;
		while ((i = reader.read()) != 0) {
			char c = (char) i;
			if (c == 'q') { // 退出
				break;
			} else
				System.out.print(c);
		}
	}
}

class MyPipedWriter implements Runnable {
	PipedWriter pipedWriter;

	public MyPipedWriter(PipedWriter pipedWriter) {
		this.pipedWriter = pipedWriter;
	}

	@Override
	public void run() {
		try {
			write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write() throws IOException {
		int i;
		while ((i = System.in.read()) != 0) {
			pipedWriter.write(i);
			if ((char) i == 'q') { // 退出
				break;
			}
		}
	}
}