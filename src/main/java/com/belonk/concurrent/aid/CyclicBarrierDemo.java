package com.belonk.concurrent.aid;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2017/9/20.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class CyclicBarrierDemo {
	//~ Static fields/initializers =====================================================================================


	//~ Instance fields ================================================================================================


	//~ Constructors ===================================================================================================


	//~ Methods ========================================================================================================

	public void test1() {
		int N = 4;
		CyclicBarrier barrier = new CyclicBarrier(N);
		for (int i = 0; i < N; i++)
			new Writer(barrier).start();
	}

	public void test2() {
		int N = 4;
		// N为线程数量，所有线程都需要调用CyclicBarrier.await后才能继续执行，否则阻塞
		// 第二个参数为所有线程到达屏障点过后执行的逻辑，Runnable内的代码会由CyclicBarrier选择某一个线程来执行，然后才会释放所有线程
		CyclicBarrier barrier = new CyclicBarrier(N, () -> System.out.println("最后执行的线程：" + Thread.currentThread().getName()));

		System.out.println("第一组线程");
		for (int i = 0; i < N; i++)
			new Writer(barrier).start();

		System.out.println("等待2秒");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 等待2秒后，重用CyclicBarrier
		System.out.println("第二组线程，重用CyclicBarrier");
		for (int i = 0; i < N; i++)
			new Writer(barrier).start();
	}

	public void test3() throws InterruptedException {
		float[][] matrix = {{1.0f, 1.1f, 1.2f}, {2.0f, 2.1f, 2.2f}, {3.1f, 3.2f, 3.3f}};
		Solver solver = new Solver(matrix);
	}

	public static void main(String[] args) throws InterruptedException {
		CyclicBarrierDemo cyclicBarrierTest = new CyclicBarrierDemo();
		// cyclicBarrierTest.test1();
		cyclicBarrierTest.test2();
		// cyclicBarrierTest.test3();
	}

}

class Writer extends Thread {
	private final CyclicBarrier cyclicBarrier;

	public Writer(CyclicBarrier cyclicBarrier) {
		this.cyclicBarrier = cyclicBarrier;
	}

	@Override
	public void run() {
		System.out.println("线程" + Thread.currentThread().getName() + "正在写入数据...");
		try {
			//以睡眠来模拟写入数据操作
			Thread.sleep(5000);
			System.out.println("线程" + Thread.currentThread().getName() + "写入数据完毕，等待其他线程写入完毕");
			// 执行完就等待其他线程到达屏障点
			int await = cyclicBarrier.await();
			System.out.println("当前等到的线程数：" + await);
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
}

class Solver {
	final int N;
	final float[][] data;
	final CyclicBarrier barrier;

	public Solver(float[][] matrix) throws InterruptedException {
		data = matrix;
		N = matrix.length;
		barrier = new CyclicBarrier(N, this::mergeRows);

		List<Thread> threads = new ArrayList<>(N);
		for (int i = 0; i < N; i++) {
			Thread thread = new Thread(new Worker(i));
			threads.add(thread);
			thread.start();
		}

		// wait until done
		for (Thread thread : threads)
			thread.join();
	}

	public void processRow(int row) {
		System.out.println(Thread.currentThread().getName() + "，处理行: " + row);
	}

	public void mergeRows() {
		System.out.println("行处理完成，开始合并...");
		System.out.println(Thread.currentThread().getName() + "，合并行...");
	}

	class Worker implements Runnable {
		int myRow;

		Worker(int row) {
			myRow = row;
		}

		public void run() {
			processRow(myRow);
			try {
				barrier.await();
			} catch (InterruptedException | BrokenBarrierException ex) {
				ex.printStackTrace();
			}
		}
	}
}