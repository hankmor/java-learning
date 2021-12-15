package com.belonk.io.file;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by sun on 2017/9/20.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class FileLineReader {
	//~ Static fields/initializers =====================================================================================


	//~ Instance fields ================================================================================================


	//~ Constructors ===================================================================================================


	//~ Methods ========================================================================================================
	private int threadSize;
	private String charset;
	private int bufferSize;
	private IHandler handler;
	private ExecutorService executorService;
	private long fileLength;
	private RandomAccessFile randomAccessFile;
	private Set<StartEndPair> startEndPairs;
	private CyclicBarrier cyclicBarrier;
	private AtomicLong counter = new AtomicLong(0);

	private FileLineReader(File file, IHandler handler, String charset, int bufferSize, int threadSize) {
		this.fileLength = file.length();
		this.handler = handler;
		this.charset = charset;
		this.bufferSize = bufferSize;
		this.threadSize = threadSize;
		try {
			this.randomAccessFile = new RandomAccessFile(file, "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.executorService = Executors.newFixedThreadPool(threadSize);
		startEndPairs = new HashSet<StartEndPair>();
	}

	public void start() {
		long everySize = this.fileLength / this.threadSize;
		try {
			calculateStartEnd(0, everySize);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		final long startTime = System.currentTimeMillis();
		cyclicBarrier = new CyclicBarrier(startEndPairs.size(), new Runnable() {
			@Override
			public void run() {
				System.out.println("use time: " + (System.currentTimeMillis() - startTime));
				System.out.println("all line: " + counter.get());
				shutdown();
			}
		});
		for (StartEndPair pair : startEndPairs) {
			System.out.println("分配分片：" + pair);
			this.executorService.execute(new SliceReaderTask(pair));
		}
	}

	private void calculateStartEnd(long start, long size) throws IOException {
		if (start > fileLength - 1) {
			return;
		}
		StartEndPair pair = new StartEndPair();
		pair.start = start;
		long endPosition = start + size - 1;
		if (endPosition >= fileLength - 1) {
			pair.end = fileLength - 1;
			startEndPairs.add(pair);
			return;
		}

		randomAccessFile.seek(endPosition);
		byte tmp = (byte) randomAccessFile.read();
		while (tmp != '\n' && tmp != '\r') {
			endPosition++;
			if (endPosition >= fileLength - 1) {
				endPosition = fileLength - 1;
				break;
			}
			randomAccessFile.seek(endPosition);
			tmp = (byte) randomAccessFile.read();
		}
		pair.end = endPosition;
		startEndPairs.add(pair);

		calculateStartEnd(endPosition + 1, size);
	}

	public void shutdown() {
		try {
			this.randomAccessFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.executorService.shutdown();
	}

	private void handle(byte[] bytes) throws UnsupportedEncodingException {
		String line = null;
		if (this.charset == null) {
			line = new String(bytes);
		} else {
			line = new String(bytes, charset);
		}
		if (!"".equals(line)) {
			this.handler.handle(line);
			counter.incrementAndGet();
		}
	}

	private static class StartEndPair {
		public long start;
		public long end;

		@Override
		public String toString() {
			return "star=" + start + ", end=" + end;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (end ^ (end >>> 32));
			result = prime * result + (int) (start ^ (start >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			StartEndPair other = (StartEndPair) obj;
			if (end != other.end)
				return false;
			if (start != other.start)
				return false;
			return true;
		}
	}

	private class SliceReaderTask implements Runnable {
		private long start;
		private long sliceSize;
		private byte[] readBuff;

		public SliceReaderTask(StartEndPair pair) {
			this.start = pair.start;
			this.sliceSize = pair.end - pair.start + 1;
			this.readBuff = new byte[bufferSize];
		}

		@Override
		public void run() {
			try {
				MappedByteBuffer mapBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, start, this.sliceSize);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				for (int offset = 0; offset < sliceSize; offset += bufferSize) {
					int readLength;
					if (offset + bufferSize <= sliceSize) {
						readLength = bufferSize;
					} else {
						readLength = (int) (sliceSize - offset);
					}
					mapBuffer.get(readBuff, 0, readLength);
					for (int i = 0; i < readLength; i++) {
						byte tmp = readBuff[i];
						if (tmp == '\n' || tmp == '\r') {
							if (tmp == '\r') {
								handle(bos.toByteArray());
								bos.reset();
							}
						} else {
							bos.write(tmp);
						}
					}
				}
				if (bos.size() > 0) {
					handle(bos.toByteArray());
				}
				bos.close();
				mapBuffer.clear();
				cyclicBarrier.await();//测试性能用
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static class Builder {
		private int threadSize = 1;
		private String charset = null;
		private int bufferSize = 1024 * 1024;
		private IHandler handle;
		private File file;

		public Builder(String file, IHandler handle) {
			this.file = new File(file);
			if (!this.file.exists())
				throw new IllegalArgumentException("文件不存在！");
			this.handle = handle;
		}

		public Builder withTreahdSize(int size) {
			this.threadSize = size;
			return this;
		}

		public Builder withCharset(String charset) {
			this.charset = charset;
			return this;
		}

		public Builder withBufferSize(int bufferSize) {
			this.bufferSize = bufferSize;
			return this;
		}

		public FileLineReader build() {
			return new FileLineReader(this.file, this.handle, this.charset, this.bufferSize, this.threadSize);
		}
	}
}
