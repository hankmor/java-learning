package com.belonk.concurrent.synchronize;

/**
 * 一个线程访问一个对象中的synchronized(this)同步代码块时，其他试图访问该对象（同一个对象）的线程将被阻塞，因为他们使用的是
 * 同一把锁，此时锁住的对象实例。
 * <p>
 * 如果实例不同，那么this指代了多个锁，不同锁的线程互不影响。
 * <p>
 * Created by sun on 2018/4/2.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class SyncThread implements Runnable {
	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Static fields/constants/initializer
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */

	private static int count;

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Instance fields
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */



	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Constructors
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */

	public SyncThread() {
		count = 0;
	}

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Public Methods
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */

	@Override
	public void run() {
		// 锁住对象
		synchronized (this) {
			for (int i = 0; i < 5; i++) {
				System.out.println(Thread.currentThread().getName() + " : " + count++);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static int getCount() {
		return count;
	}

	public static void main(String[] args) throws InterruptedException {
		// 两个线程访问同一个对象实例，使用的是同一把锁，它们是互斥的
		SyncThread syncThread = new SyncThread();
		Thread thread1 = new Thread(syncThread, "thread1");
		Thread thread2 = new Thread(syncThread, "thread2");
		thread1.start();
		thread2.start();
		Thread.sleep(5000);
		System.out.println("====================================");
		// 两个线程分别访问不同的对象实例，由于锁不同，所以他们之间互不影响
		SyncThread syncThread1 = new SyncThread();
		SyncThread syncThread2 = new SyncThread();
		new Thread(syncThread1, "thread3").start();
		new Thread(syncThread2, "thread4").start();
	}

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Private Methods
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */


}
/*~:out
thread1 : 0
thread1 : 1
thread1 : 2
thread1 : 3
thread1 : 4
thread2 : 5
thread2 : 6
thread2 : 7
thread2 : 8
thread2 : 9
====================================
thread3 : 0
thread4 : 1
thread4 : 2
thread3 : 3
thread4 : 5
thread3 : 4
thread3 : 6
thread4 : 7
thread3 : 8
thread4 : 9
:out!*/