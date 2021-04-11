package com.belonk.concurrent.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁demo。
 * <p>
 * 读锁：共享锁，写锁：独占锁，是一种读写分离的思想，写数据的时候线程不共享，其他时候线程可共享读取数据，提高并发量。
 * <p>
 * 针对同一个数据，线程间操作它的关系如下：
 * 读 - 读：线程间可以同时进行，即都可读
 * 读 - 写：线程间不能同时进行，只能有一个线程加锁写
 * 写 - 写：线程间不能同时进行，只能有一个线程加锁写
 * <p>
 * Created by sun on 2020/7/21.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ReadWriteLockDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		ReadWriteLockDemo demo = new ReadWriteLockDemo();
		// demo.unsafeCache();
		demo.safeCache();
	}

	/**
	 * 缓存未实现加锁，导致写数据时会被其他线程中断。线程不安全
	 */
	private void unsafeCache() {
		UnsafeCache unsafeCache = new UnsafeCache();

		for (int i = 1; i <= 8; i++) {
			int finalI = i;
			new Thread(() -> {
				unsafeCache.get("key" + finalI);
			}, "Read Thread[" + i + "]").start();
		}

		for (int i = 1; i <= 8; i++) {
			int finalI = i;
			new Thread(() -> {
				unsafeCache.put(finalI + "", new Object());
			}, "Write Thread[" + i + "]").start();
		}
	}

	private void safeCache() {
		SafeCache safeCache = new SafeCache();
		for (int i = 1; i <= 8; i++) {
			int finalI = i;
			new Thread(() -> {
				safeCache.get("key" + finalI);
			}, "Read Thread[" + i + "]").start();
		}

		for (int i = 1; i <= 8; i++) {
			int finalI = i;
			new Thread(() -> {
				safeCache.put(finalI + "", new Object());
			}, "Write Thread[" + i + "]").start();
		}
	}
}

class UnsafeCache {
	private final Map<String, Object> map = new HashMap<>();

	public void put(String key, Object val) {
		System.out.println(Thread.currentThread().getName() + "\t正在写数据");
		map.put(key, val);
		try {
			// 模拟写入时间
			TimeUnit.MILLISECONDS.sleep((long) ((Math.random() * 1000) + 2));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + "\t写入完成");
	}

	public Object get(String key) {
		System.out.println(Thread.currentThread().getName() + "\t正在读数据");
		Object ret = map.get(key);
		try {
			// 模拟读取时间
			TimeUnit.MILLISECONDS.sleep((long) ((Math.random() * 1000) + 1));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName() + "\t读取完成");
		return ret;
	}
}

class SafeCache {
	private final Map<String, Object> map = new HashMap<>();

	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	/**
	 * 读锁
	 */
	private final Lock readLock = readWriteLock.readLock();

	/**
	 * 写锁
	 */
	private final Lock writeLock = readWriteLock.writeLock();

	public void put(String key, Object val) {
		try {
			// 加写锁
			writeLock.lock();
			System.out.println(Thread.currentThread().getName() + "\t正在写数据");
			map.put(key, val);

			// 模拟写入时间
			TimeUnit.MILLISECONDS.sleep((long) ((Math.random() * 1000) + 2));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			writeLock.unlock();
			System.out.println(Thread.currentThread().getName() + "\t写入完成");
		}
	}

	public Object get(String key) {
		Object ret = null;
		try {
			// 加读锁
			readLock.lock();
			System.out.println(Thread.currentThread().getName() + "\t正在读数据");
			ret = map.get(key);

			// 模拟读取时间
			TimeUnit.MILLISECONDS.sleep((long) ((Math.random() * 1000) + 1));
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println(Thread.currentThread().getName() + "\t读取完成");
			readLock.unlock();
		}
		return ret;
	}
}