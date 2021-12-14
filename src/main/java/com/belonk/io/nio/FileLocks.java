package com.belonk.io.nio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/12/14.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class FileLocks {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 文件加锁
	 *
	 * 通过FileChannel的tryLock和lock方法获得锁，前者无阻塞加锁，后者则会阻塞进程直到获得锁或者调用lock的线程中断或者通道关闭。
	 *
	 * 独占锁：其他进程不能操作被锁定的文件
	 * 共享锁：其他进程可以读取被锁的文件
	 *
	 * 独占锁和共享锁的实现有操作系统底层支持，如果不支持共享锁则会使用独占锁。
	 */

	public static void main(String[] args) throws IOException, InterruptedException {
		FileOutputStream outputStream = new FileOutputStream("lockedFile");
		// 无阻塞加锁，当前不能获得锁则返回null
		FileLock fileLock = outputStream.getChannel().tryLock();
		if (fileLock != null) {
			System.out.println("isShared: " + fileLock.isShared());
			System.out.println("File locked...");
			TimeUnit.SECONDS.sleep(1);
			fileLock.release();
			System.out.println("File lock released");
		}

		// 阻塞加锁，必须获得锁或者线程中断或者FileChannel关闭才会返回
		FileLock fileLock1 = outputStream.getChannel().lock();
		System.out.println("File locked...");
		TimeUnit.SECONDS.sleep(1);
		fileLock1.release();
		System.out.println("File lock released");

		outputStream.close();
		new File("lockedFile").delete();
	}
}