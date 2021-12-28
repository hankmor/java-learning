package com.belonk.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sun on 2021/12/28.
 *
 * @author sunfuchang03@126.com
 * @see CarWaxing
 * @see CarWaxing2
 * @since 1.0
 */
public class CarWaxing3 {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 使用 Lock 和 Condition 重新实现的汽车打蜡案例。
	 */

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newCachedThreadPool();
		Car2 car = new Car2();
		executorService.execute(new WaxOn2(car));
		executorService.execute(new WaxOff2(car));
		TimeUnit.SECONDS.sleep(2);
		executorService.shutdownNow();
	}
}

class Car2 {
	// 创建锁
	Lock lock = new ReentrantLock();
	// 创建条件
	Condition condition = lock.newCondition();
	private boolean waxed = false;

	void waxed() {
		lock.lock();
		try {
			waxed = true;
			// 通知清洗
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	void buffed() {
		lock.lock();
		try {
			waxed = false;
			// 通知打蜡
			condition.signalAll();
		} finally {
			lock.unlock();
		}
	}

	void waitForBuffing() throws InterruptedException {
		lock.lock();
		try {
			while (waxed) {
				this.condition.await();
			}
		} finally {
			lock.unlock();
		}
	}

	void waitForWaxing() throws InterruptedException {
		lock.lock();
		try {
			while (!waxed) {
				this.condition.await();
			}
		} finally {
			lock.unlock();
		}
	}
}

class WaxOn2 implements Runnable {
	private Car2 car;

	public WaxOn2(Car2 car) {
		this.car = car;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				// 打蜡中...
				System.out.print("WaxOn! ");
				TimeUnit.MILLISECONDS.sleep(100);
				// 更新状态
				this.car.waxed();
				// 等待清洗完成
				this.car.waitForBuffing();
			}
		} catch (InterruptedException e) {
			System.out.print("WaxOn：任务中断！");
		}
	}
}

class WaxOff2 implements Runnable {
	private Car2 car;

	public WaxOff2(Car2 car) {
		this.car = car;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				// 等待打蜡完成
				this.car.waitForWaxing();
				TimeUnit.MILLISECONDS.sleep(100);
				System.out.print("WaxOff! ");
				// 更新状态
				this.car.buffed();
			}
		} catch (InterruptedException e) {
			System.out.print("WaxOff：任务中断！");
		}
	}
}