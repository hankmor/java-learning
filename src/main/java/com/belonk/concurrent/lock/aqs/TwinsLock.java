package com.belonk.concurrent.lock.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 自定义锁，每次只能有两个线程访问该锁，超过的线程将被阻塞。
 * <p>
 * Created by sun on 2021/5/3.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class TwinsLock {
	//~ Static fields/constants/initializer


	//~ Instance fields

	private final Sync sync;

	//~ Constructors

	public TwinsLock() {
		// 初始化状态为2，表示只有两个线程能够获取到锁
		sync = new Sync(2);
	}

	//~ Methods

	public void lock() {
		sync.acquireShared(1);
	}

	public void unlock() {
		sync.releaseShared(1);
	}

	// 同时有两个线程获取到所，应该使用共享式的同步状态
	static class Sync extends AbstractQueuedSynchronizer {
		public Sync(int permits) {
			setState(permits);
		}

		/*
		该方法返回值：
		1、负数：表示获取共享状态失败
		2、0：获取共享状态成功，但后续没有其他线程能够获取共享状态了
		3、正数：当前线程获取成功，并且可供其他线程使用的共享状态数量，后续其他线程也可以获取成功
		*/
		@Override
		protected int tryAcquireShared(int acquires) {
			// 简单实现：循环获取同步状态，直到成功
			while (true) {
				int available = getState();
				int remaining = available - acquires;
				if (remaining < 0 || compareAndSetState(available, remaining))
					return remaining;
			}
		}

		@Override
		protected boolean tryReleaseShared(int acquires) {
			// 释放锁，必须保证释放成功
			while (true) {
				int current = getState();
				int next = current + acquires;
				if (next < current) // overflow
					throw new Error("Maximum permit count exceeded");
				if (compareAndSetState(current, next))
					return true;
			}
		}
	}

	public static void main(String[] args) {
		TwinsLock lock = new TwinsLock();
		for (int i = 0; i < 20; i++) {
			new Thread(() -> {
				lock.lock();
				try {
					System.out.println(Thread.currentThread().getName());
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}, "Thread " + i).start();
		}
		/*
		结果：同时打印两个线程，然后2秒后再打印两个线程
		 */
	}
}