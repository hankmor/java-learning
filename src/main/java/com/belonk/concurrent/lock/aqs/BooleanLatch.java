package com.belonk.concurrent.lock.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by sun on 2021/5/2.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class BooleanLatch {
	//~ Static fields/constants/initializer


	//~ Instance fields

	private final Sync sync = new Sync();

	//~ Constructors


	//~ Methods

	public void signal() {
		sync.releaseShared(1);
	}

	public void isSignalled() {
		sync.isSignalled();
	}

	public void await() throws InterruptedException {
		sync.acquireSharedInterruptibly(1);
	}

	/*
	state默认是0，释放锁后设置为1，获取锁时检查state是否已经释放，释放了则返回1，表示可以成功获取锁，否则返回-1，表示不能获取锁
	 */
	static class Sync extends AbstractQueuedSynchronizer {
		public boolean isSignalled() {
			return getState() != 0;
		}

		@Override
		protected int tryAcquireShared(int acquires) {
			// 如果通知成功（即获取锁成功），返回1，否则返回-1
			return isSignalled() ? 1 : -1;
		}

		@Override
		protected boolean tryReleaseShared(int acquires) {
			setState(1);
			return true;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		BooleanLatch latch = new BooleanLatch();
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				try {
					// 睡眠1秒后，通知线程
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				latch.signal();

				System.out.println(Thread.currentThread().getName());

				try {
					// 等待，此时线程都被阻塞
					latch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}, "thread " + i).start();
		}
		System.out.println("main thread running");
	}
}