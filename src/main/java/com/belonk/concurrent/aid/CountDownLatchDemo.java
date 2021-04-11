package com.belonk.concurrent.aid;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2020/7/23.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class CountDownLatchDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws InterruptedException {
		Driver driver = new Driver();
		driver.doWork();
		System.out.println("==============");
		Driver2 driver2 = new Driver2();
		driver2.doWork();
		System.out.println("==============");
		Driver3 driver3 = new Driver3();
		driver3.doWork();

	}
}

class Driver {
	public void doWork() throws InterruptedException {
		int threadCount = 10;
		CountDownLatch doneSignal = new CountDownLatch(threadCount);
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		System.out.println("创建分支线程...");
		// 创建并启动分支线程
		for (int i = 0; i < threadCount; ++i)
			executorService.execute(new Worker(doneSignal));

		doSomethingElse();
		// 等待分支线程全部工作完成
		doneSignal.await();
		// 主线程再执行一些逻辑
		doAnotherSomethingElse();
		System.out.println("主线程和分支线程全部工作完成");
		executorService.shutdown();
		/*~:
		 * 期望的结果：创建分支线程 -> 主线程工作或者分支线程工作 —> 主线程再工作 -> 全部结束
		 */
	}

	private void doAnotherSomethingElse() {
		System.out.println("再做了一些其他工作");
	}

	private void doSomethingElse() {
		System.out.println("做一些其他工作...");
	}
}

class Worker implements Runnable {
	private final CountDownLatch doneSignal;

	Worker(CountDownLatch doneSignal) {
		this.doneSignal = doneSignal;
	}

	public void run() {
		// 线程开始工作
		doWork();
		// 当前线程工作完成，latch减1
		doneSignal.countDown();
	}

	void doWork() {
		System.out.println(Thread.currentThread().getName() + "/t开始工作了...");
	}
}

class Driver2 {
	public void doWork() throws InterruptedException {
		int threadCount = 10;
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(threadCount);

		System.out.println("创建分支线程...");
		// 创建并启动分支线程
		for (int i = 0; i < threadCount; ++i)
			new Thread(new Worker2(startSignal, doneSignal), "线程[" + i + "]").start();

		// 主线程工作，分支线程必须等主线程调用countDown之后才能开始
		doSomethingElse();            // don't let run yet
		// 主线程工作完成，latch减1，此时其他线程可以工作了
		startSignal.countDown();      // let all threads proceed
		// 等待分支线程全部工作完成
		doneSignal.await();           // wait for all to finish
		// 主线程再执行一些逻辑
		doAnotherSomethingElse();
		System.out.println("主线程和分支线程全部工作完成");

		/*~:
		 * 期望的结果：创建分支线程 -> 主线程工作 -> 分支线程工作 —> 主线程工作 -> 全部结束
		 */
	}

	private void doAnotherSomethingElse() {
		System.out.println("再做了一些其他工作");
	}

	private void doSomethingElse() {
		System.out.println("做一些其他工作...");
	}
}

class Worker2 implements Runnable {
	private final CountDownLatch startSignal;
	private final CountDownLatch doneSignal;

	Worker2(CountDownLatch startSignal, CountDownLatch doneSignal) {
		this.startSignal = startSignal;
		this.doneSignal = doneSignal;
	}

	public void run() {
		try {
			// 主线程等待，当前线程要等到startSignal调用countDown才能继续向下执行
			startSignal.await();
			// 线程开始工作
			doWork();
			// 当前线程工作完成，latch减1
			doneSignal.countDown();
		} catch (InterruptedException ex) {
		}
	}

	void doWork() {
		System.out.println(Thread.currentThread().getName() + "/t开始工作了...");
	}
}

class Driver3 {
	public void doWork() throws InterruptedException {
		int threadCount = 10;
		CountDownLatch doneSignal = new CountDownLatch(threadCount);
		System.out.println("创建第一组分支线程...");
		// 创建并启动分支线程
		for (int i = 0; i < threadCount; ++i)
			new Thread(new Worker3(doneSignal)).start();

		// 等待分支线程全部工作完成
		doneSignal.await();
		// 主线程再执行一些逻辑
		doSomethingElse();
		System.out.println("主线程和分支线程全部工作完成");

		// 注意以下是错误示例：重用CountDownLatch后，结果不正确

		System.out.println("====2秒后第二组开始====");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("创建第二组分支线程");
		// 创建并启动分支线程
		for (int i = 0; i < threadCount; ++i)
			new Thread(new Worker3(doneSignal)).start();

		// 等待分支线程全部工作完成
		doneSignal.await();
		// 主线程再执行一些逻辑
		doSomethingElse();
		System.out.println("主线程和分支线程全部工作完成");

		/*!
		 * 可能的结果如下：
		 * 做一些其他工作...
		 * 主线程和分支线程全部工作完成
		 * Thread-19/t开始工作了...
		 *
		 * 可以看到，重用CountDownLatch后，结果不正确！doneSignal已经不能控制线程的等待了，分支线程可能在主线程完成后才开始执行。
		 *
		 * 所以，CountDownLatch是不能重用的，计数器减为0过后，不能保证线程的相互等待状态了
		 */
	}

	private void doSomethingElse() {
		System.out.println("做一些其他工作...");
	}
}

class Worker3 implements Runnable {
	private final CountDownLatch doneSignal;

	Worker3(CountDownLatch doneSignal) {
		this.doneSignal = doneSignal;
	}

	public void run() {
		// 线程开始工作
		doWork();
		// 当前线程工作完成，latch减1
		doneSignal.countDown();
		System.out.println("当前计数器值：" + doneSignal.getCount());
	}

	void doWork() {
		System.out.println(Thread.currentThread().getName() + "/t开始工作了...");
	}
}