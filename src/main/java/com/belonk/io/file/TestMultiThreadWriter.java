package com.belonk.io.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

class Creater implements Runnable {
	private ConcurrentLinkedQueue<String> queue;
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	private int start;
	private int row;

	public Creater(int start, int row, ConcurrentLinkedQueue<String> queue) {
		this.queue = queue;
		this.row = row;
		this.start = start;
	}

	@Override
	public void run() {
		String thread = Thread.currentThread().getName() + "_" + Thread.currentThread().getId();
		System.out.println(thread + " start writing ...");
		System.out.println("start : " + start);
		for (int i = start; i <= row + start; i++) {
			String contents = i + "_" + LocalDateTime.now().format(formatter);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			queue.add(contents);
		}
		System.out.println("end : " + row + start);
		System.out.println(thread + " writed finished.");
	}
}

class DealFile implements Runnable {
	private FileOutputStream out;
	private ConcurrentLinkedQueue<String> queue;

	public DealFile() {
	}

	public DealFile(FileOutputStream out, ConcurrentLinkedQueue<String> queue) {
		this.out = out;
		this.queue = queue;
		new Timer("flush").schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}, 0, 5000);
	}

	@Override
	public void run() {
		while (true) {//循环监听
			if (!queue.isEmpty()) {
				try {
					out.write(queue.poll().getBytes());
					out.write("\r\n".getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

/**
 * Created by sun on 2018/2/6.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class TestMultiThreadWriter {
	//~ Static fields/initializers =====================================================================================


	//~ Instance fields ================================================================================================


	//~ Constructors ===================================================================================================


	//~ Methods ========================================================================================================

	public static void main(String[] args) throws FileNotFoundException {
		FileOutputStream out = new FileOutputStream(new File("F:" + File.separator + "test.txt"), true);
		ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();

//        CountDownLatch countDownLatch = new CountDownLatch(10);
		//多线程往队列中写入数据
		int row = 100000;
		int start = 0;
		for (int j = 0; j < 10; j++) {
			start = j * row + 1;
			new Thread(new Creater(start, row, queue)).start();
		}
		//监听线程，不断从queue中读数据写入到文件中
		new Thread(new DealFile(out, queue)).start();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));
	}
}


