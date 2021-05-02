package com.belonk.concurrent.lock.aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 独占锁实现，一次只能有一个线程获得锁。
 * <p>
 * Created by sun on 2021/5/2.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class Mutex implements Lock {
	//~ Static fields/constants/initializer


	//~ Instance fields

	private final Sync sync;

	//~ Constructors

	public Mutex() {
		this.sync = new Sync();
	}

	//~ Methods

	// Api实现只需要将发放委托给Sync即可

	@Override
	public void lock() {
		sync.acquire(1);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireInterruptibly(1);
	}

	@Override
	public boolean tryLock() {
		return sync.tryAcquire(1);
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return sync.tryAcquireNanos(1, unit.toNanos(time));
	}

	@Override
	public void unlock() {
		sync.release(1);
	}

	@Override
	public Condition newCondition() {
		return sync.newCondition();
	}

	static class Sync extends AbstractQueuedSynchronizer {
		@Override
		protected boolean tryAcquire(int acquires) {
			// 只支持一个线程获取同步状态，值为1
			if (acquires != 1)
				throw new IllegalArgumentException();
			// 原子操作，将同步状态从0设置为1，成功则表明获取同步状态(锁)成功，否则获取失败，表明有其他线程占有了锁
			return compareAndSetState(0, 1);
		}

		@Override
		protected boolean tryRelease(int acquires) {
			// 同步状态释放时，其值必须为1
			if (acquires != 1)
				throw new IllegalArgumentException();
			// 释放时，当前线程已经持有锁，故不需要原子操作
			// if (compareAndSetState(1, 0)) {
			// 	setExclusiveOwnerThread(null);
			// }
			// 同步状态设置为0
			setState(0);
			// 当前独占线程设置为null
			setExclusiveOwnerThread(null);
			return true;
		}

		@Override
		protected boolean isHeldExclusively() {
			// 如果同步状态是1，那么表明当前线程持有锁
			// return getState() == 1;
			// 也可以通过独占线程判断
			return getExclusiveOwnerThread() == Thread.currentThread();
		}

		public Condition newCondition() {
			return new ConditionObject();
		}
	}
}