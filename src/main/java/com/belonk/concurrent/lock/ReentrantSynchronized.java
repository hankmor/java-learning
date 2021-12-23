package com.belonk.concurrent.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sun on 2021/12/23.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ReentrantSynchronized {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 本示例展示了 synchronized 锁的重入
	 */

	public static void main(String[] args) {
		ReentrantSynchronized rs = new ReentrantSynchronized();
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		// 一共调用10次
		executorService.execute(() -> rs.f1(10));
		executorService.shutdown();

		/*
		f1 calling f2, cnt: 9
		f2 calling f1, cnt: 8
		f1 calling f2, cnt: 7
		f2 calling f1, cnt: 6
		f1 calling f2, cnt: 5
		f2 calling f1, cnt: 4
		f1 calling f2, cnt: 3
		f2 calling f1, cnt: 2
		f1 calling f2, cnt: 1
		f2 calling f1, cnt: 0
		 */
	}

	// 两个互相调用的同步方法，synchronized 锁都是 this
	// 通过cnt计数器来追踪调用次数，每调用一次计数器减1

	public synchronized void f1(int cnt) {
		if (cnt-- > 0) {
			System.out.println("f1 calling f2, cnt: " + cnt);
			f2(cnt);
		}
	}

	public synchronized void f2(int cnt) {
		if (cnt-- > 0) {
			System.out.println("f2 calling f1, cnt: " + cnt);
			f1(cnt);
		}
	}
}