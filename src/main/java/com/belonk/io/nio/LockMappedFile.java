package com.belonk.io.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.concurrent.CountDownLatch;

/**
 * Created by sun on 2021/12/14.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class LockMappedFile {
	//~ Static fields/constants/initializer

	private static final int length = 0xFFFFFF; // 约140M大小

	//~ Instance fields

	private FileChannel fileChannel;

	//~ Constructors


	//~ Methods

	/*
	 * 对内存映射文件加锁。
	 *
	 * 主要用到FileChannel的lock(long position, long size, boolean shared) 或 tryLock(long position, long size, boolean shared)
	 * 方法进行部分加锁。
	 */

	public static void main(String[] args) throws IOException {
		String file = "lockMappedFile.dat";
		LockMappedFile lockMappedFile = new LockMappedFile();
		lockMappedFile.modify(file);
	}

	public void modify(String file) throws IOException {
		System.out.println("Total size: " + length);
		this.fileChannel = new RandomAccessFile(file, "rw").getChannel();
		MappedByteBuffer mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, length);
		for (int i = 0; i < length; i++) {
			mappedByteBuffer.put((byte) 'a');
		}
		System.out.println("Write file finished");

		CountDownLatch countDownLatch = new CountDownLatch(2);
		// 线程修改文件的前1/4部分
		int start = 0;
		int end = length / 4;
		new Thread(new MappedFileModifier(mappedByteBuffer, start, end, countDownLatch)).start();
		// 线程修改文件的中间部分：从1/2位置开始，到 1/2 + 1/4位置结束
		int start1 = length / 2;
		int end1 = start1 + length / 4;
		new Thread(new MappedFileModifier(mappedByteBuffer, start1, end1, countDownLatch)).start();

		try {
			countDownLatch.await();
			// 读取修改的内容
			mappedByteBuffer.rewind();
			for (int i = start; i < end - 1; i++) {
				char c = (char) mappedByteBuffer.get(i);
				if (c != 'b') {
					System.out.println(c);
					throw new RuntimeException();
				}
			}
			System.out.println("====");
			mappedByteBuffer.rewind();
			for (int i = start1; i < end1 - 1; i++) {
				char c = (char) mappedByteBuffer.get(i);
				if (c != 'b') {
					throw new RuntimeException();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private class MappedFileModifier implements Runnable {
		// 操作的文件部分起止位置
		private final int start;
		private final int end;
		private final ByteBuffer byteBuffer;
		private final CountDownLatch countDownLatch;

		MappedFileModifier(MappedByteBuffer mbb, int start, int end, CountDownLatch countDownLatch) {
			this.start = start;
			this.end = end;
			// 注意这里先要设置limit，因为设置position是会检查limit必须大于新设置的position
			mbb.limit(end);
			mbb.position(start);
			// slice方法创建一个原缓冲区的共享子序列，对其的修改会反映到原缓冲区，具有单独的position、capacity、limit等
			this.byteBuffer = mbb.slice();

			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			try {
				// 锁定start、end指定的文件部分
				FileLock lock = fileChannel.lock(this.start, this.end, false);
				System.out.println("lock file from " + this.start + " to " + this.end);
				System.out.println("modifying...");
				// 修改内容
				for (int i = 0; i < end - start - 1; i++) {
					byteBuffer.put(i, (byte) (byteBuffer.get(i) + 1));
				}
				// 释放锁
				lock.release();
				System.out.println("release file from " + this.start + " to " + this.end);
				this.countDownLatch.countDown();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}