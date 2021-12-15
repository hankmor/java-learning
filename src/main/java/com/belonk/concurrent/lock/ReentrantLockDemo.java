package com.belonk.concurrent.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sun on 2021/7/31.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ReentrantLockDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws InterruptedException {
		Res res = new Res();
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		// 5个线程
		for (int i = 0; i < 5; i++) {
			// 每个线程对它加1000次
			for (int j = 0; j < 1000; j++) {
				executorService.submit((Runnable) res::add);
			}
		}
		// 睡眠3秒，充分执行
		TimeUnit.SECONDS.sleep(3);
		System.out.println(res.getI());
		executorService.shutdown();
	}
}

class Res {
	private int i;
	private static final ReentrantLock lock = new ReentrantLock();

	public int add() {
		lock.lock();
		try {
			return ++i;
		} finally {
			lock.unlock();
		}
	}

	public int getI() {
		return i;
	}
}