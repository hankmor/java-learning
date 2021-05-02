package com.belonk.concurrent.lock.aqs;

import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/5/2.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class MutexTest {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws InterruptedException {
		Mutex mutex = new Mutex();
		int[] count = {0};
		// 10个线程
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				// 加1000次
				for (int j = 0; j < 1000; j++) {
					mutex.lock();
					try {
						++count[0];
					} finally {
						mutex.unlock();
					}
				}
			}).start();
		}
		TimeUnit.SECONDS.sleep(3);
		System.out.println(count[0]);
		System.out.println(count[0] == 10 * 1000);
	}
}