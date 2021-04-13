package com.belonk.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by sun on 2020/7/21.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class SpinLockDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods


	public static void main(String[] args) {
		/*
		 * 自旋锁演示示例
		 */
		SpinLock lock = new SpinLock();
		// 创建线程，模拟抢占锁的情况
		for (int i = 1; i <= 5; i++) {
			new Thread(() -> {
				// 加锁
				lock.lock();
				try {
					// 模拟执行业务逻辑
					System.out.println(Thread.currentThread().getName() + "已获取锁");
					TimeUnit.MILLISECONDS.sleep((long) ((Math.random() * 1000) + 2));
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					// 释放锁
					lock.unlock();
					System.out.println(Thread.currentThread().getName() + "已释放锁");
				}
			}, "thread[" + i + "]").start();
		}
	}
}

class SpinLock {
	/**
	 * 原子引用，持有<i>已获取锁</i>的线程
	 */
	private final AtomicReference<Thread> threadAtomicReference = new AtomicReference<>();

	public void lock() {
		do {
			// Thread curThread = threadAtomicReference.get();
			// 抢占锁的线程，通过CAS不断比较原子引用持有的线程是否为null，是则说明没有线程持有锁
		} while (!threadAtomicReference.compareAndSet(null, Thread.currentThread()));
	}

	public void unlock() {
		// 持有锁的线程，释放锁，即是将原子引用的线程设置为null
		threadAtomicReference.compareAndSet(Thread.currentThread(), null);
	}
}