package com.belonk.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sun on 2021/12/23.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class BlockingLockInterrupted {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 演示 JDK5 引入的 ReentrantLock 可以响应中断示例
	 */

	public static void main(String[] args) throws InterruptedException {
		// 在Main线程中创建对象，此时直接会调用lock
		ReentrantLockBlocked blocked = new ReentrantLockBlocked();
		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.execute(() -> {
			//! 不要直接在线程内创建对象，此时lock是重入加锁，可以直接获得锁
			// ReentrantLockBlocked blocked = new ReentrantLockBlocked();
			blocked.f();
		});
		TimeUnit.SECONDS.sleep(1);
		System.out.println("Send interrupt signal");
		// 直接关闭线程池，发送中断信号
		executorService.shutdownNow();
		System.out.println("Exit");
		/*
		Lock first: main
		Lock interruptibly: pool-1-thread-1
		Send interrupt signal
		Exit
		Locking thread is interrupted: pool-1-thread-1
		 */
	}
}

/**
 * 锁被阻塞
 */
class ReentrantLockBlocked {
	private final Lock lock = new ReentrantLock();

	public ReentrantLockBlocked() {
		System.out.println("Lock first: " + Thread.currentThread().getName());
		// 锁定，其他线程都不能获得锁
		lock.lock();
	}

	public void f() {
		try {
			// 可响应中断的加锁，由于锁已经被锁定，所以此处加锁的线程被阻塞
			System.out.println("Lock interruptibly: " + Thread.currentThread().getName());
			lock.lockInterruptibly();
			System.out.println("Got lock: " + Thread.currentThread().getName());
		} catch (InterruptedException e) {
			System.out.println("Locking thread is interrupted: " + Thread.currentThread().getName());
		}
	}
}