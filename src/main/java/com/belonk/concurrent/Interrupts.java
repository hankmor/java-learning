package com.belonk.concurrent;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 中断线程demo。
 * <p>
 * Created by sun on 2021/12/19.
 *
 * @author sunfuchang03@126.com
 * @see com.belonk.concurrent.thread.interrupt.ThreadInterrupted
 * @see com.belonk.concurrent.thread.interrupt.SecurelyStopThread
 * @since 1.0
 */
public class Interrupts {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 中断线程demo：包括 sleep 时中断线程、等待 IO 时中断线程、获取锁时中断。
	 *
	 * 使用线程池中断单个线程，需要调用 ExecutorService 的 submit 方法返回一个 Future 对象，然后调用其 cancel(true) 方法将中断进行
	 * 中的线程。
	 */

	private static ExecutorService executorService = Executors.newCachedThreadPool();

	public static void test(Runnable runnable) throws InterruptedException {
		Future<?> future = executorService.submit(runnable);
		// 执行一会儿
		TimeUnit.MILLISECONDS.sleep(100);
		// 中断当前线程
		System.out.println("interrupting thread: " + runnable.getClass().getSimpleName());
		future.cancel(true);
		// 发送了中断信号，是否真的被中断需要看线程中的输出
		System.out.println("interrupt sent to : " + runnable.getClass().getSimpleName());
	}

	public static void main(String[] args) throws InterruptedException {
		test(new SleepingInterrupt());
		test(new IOInterrupt(System.in));
		test(new SynchronizedBlockedInterrupt());
		TimeUnit.SECONDS.sleep(3);
		System.out.println("System exit.");
		System.exit(0);

		/*
		// SleepingInterrupt 正常中断
		interrupting thread: SleepingInterrupt
		interrupt sent to : SleepingInterrupt
		Sleeping interrupted.

		// IOInterrupt 不能被中断
		Waiting for read:
		interrupting thread: IOInterrupt
		interrupt sent to : IOInterrupt

		// 无法获取锁被阻塞的线程不能中断
		Trying to obtain lock.
		interrupting thread: SynchronizedBlockedInterrupt
		interrupt sent to : SynchronizedBlockedInterrupt
		System exit.
		 */
	}
}

/**
 * 长时间睡眠的线程被中断。
 * <p>
 * 从结果可以看出，睡眠的线程抛出了 InterruptException，可以被正常中断。
 */
class SleepingInterrupt implements Runnable {
	@Override
	public void run() {
		try {
			// 长时间睡眠
			TimeUnit.SECONDS.sleep(100);
		} catch (InterruptedException e) {
			System.out.println("Sleeping interrupted.");
		}
	}
}

/**
 * 长时间的IO操作的线程被中断.
 * <p>
 * 从结果可以看出，IO操作并没有被中断。一旦线程IO操作开始，就不能被中断，因此，IO操作可能锁住多线程程序。可以通过关闭资源来实现IO操作的
 * 中断.
 *
 * @see CloseResource
 */
class IOInterrupt implements Runnable {
	private InputStream in;

	public IOInterrupt(InputStream in) {
		this.in = in;
	}

	@Override
	public void run() {
		System.out.println("Waiting for read: ");
		try {
			// 等待输入
			in.read();
		} catch (IOException e) {
			// 尝试检测中断状态
			if (Thread.currentThread().isInterrupted()) {
				// 线程被中断时输出
				System.out.println("Blocked IO was interrupted.");
			} else {
				// IO异常，直接抛出新异常
				throw new RuntimeException();
			}
		}
		// 成功退出IO操作时输出
		System.out.println("Exiting IOInterrupt.");
	}
}

/**
 * 等待获取锁的线程被中断.
 * <p>
 * 从结果可以看出, 不能中断正在试图获取 synchronized 锁的线程。
 */
class SynchronizedBlockedInterrupt implements Runnable {
	public SynchronizedBlockedInterrupt() {
		// 单独开启一个线程，一直占有锁，this对象作为锁
		new Thread(() -> {
			synchronized (this) {
				while (true) {
					Thread.yield();
				}
			}
		}).start();
	}

	@Override
	public void run() {
		System.out.println("Trying to obtain lock.");
		// 尝试获取锁，this对象作为锁，被其他线程一直持有，当前线程获取不到锁被阻塞
		synchronized (this) {
			System.out.println("Obtained lock successfully.");
		}
		// 成功获取到锁，或者如果获取锁的过程被中断，会输出下边的信息
		System.out.println("Exiting SynchronizedBlockedInterrupt run().");
	}
}