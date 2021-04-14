package com.belonk.concurrent.thread.join;

/**
 * Created by sun on 2021/4/14.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class MultiThreadJoin implements Runnable {
	//~ Static fields/constants/initializer

	private final Thread prev;

	//~ Instance fields


	//~ Constructors

	public MultiThreadJoin(Thread prev) {
		this.prev = prev;
	}

	//~ Methods


	@Override
	public void run() {
		try {
			this.prev.join();
			System.out.println(Thread.currentThread().getName());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		/*
		thread.join：thread线程执行完成后，调用thread.join方法的线程才从thread.join方法返回。即：thread先执行完成，然后调用thread.join方法的线程才能继续执行。
		 */
		Thread prev = Thread.currentThread();
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread(new MultiThreadJoin(prev), "thread-" + i);
			prev = thread;
			thread.start();
		}
		System.out.println("main thread");

		/*
		main thread
		thread-0
		thread-1
		thread-2
		thread-3
		thread-4
		thread-5
		thread-6
		thread-7
		thread-8
		thread-9
		 */
	}
}