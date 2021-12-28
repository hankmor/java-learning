package com.belonk.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/12/28.
 *
 * @author sunfuchang03@126.com
 * @see CarWaxing
 * @since 1.0
 */
public class CarWaxing2 {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 改进案例 CarWaxing，将清洗和打蜡的任务放到具体的线程任务中。
	 */

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newCachedThreadPool();
		// 操作同一辆汽车
		Car1 car = new Car1();
		executorService.execute(new WaxOn1(car));
		executorService.execute(new WaxOff1(car));

		// 3秒后停止
		TimeUnit.SECONDS.sleep(2);
		executorService.shutdownNow();

		/*
		WaxOn! WaxOff! WaxOn! WaxOff! WaxOn! WaxOff! WaxOn! WaxOff! WaxOn! WaxOff! WaxOn! WaxOff! WaxOn! WaxOff! WaxOn! WaxOff! WaxOn! WaxOff! WaxOn! WaxOff! WaxOn：任务中断！WaxOff：任务中断！
		 */
	}
}

// 线程操作的资源：汽车
class Car1 {
	// 给定一个已经打蜡的标记，waxed为true，说明已打蜡，待清洗；waxed为false，说明已清洗，等待打蜡
	private boolean waxed = false;

	/**
	 * 更改状态，表示清洗完成。
	 * 必须加锁，包装两个方法只能同时有一个执行
	 */
	public synchronized void buffed() {
		// 更改状态
		this.waxed = false;
		// 通知清洗完成，可以（继续）打蜡了
		this.notifyAll();
	}

	/**
	 * 更改状态，表示打蜡完成。
	 * 必须加锁，包装两个方法只能同时有一个执行
	 */
	public synchronized void waxed() {
		// 更改状态
		this.waxed = true;
		// 打蜡完成，通知可以清洗了
		this.notifyAll();
	}

	/**
	 * 等待清洗完成，需要加锁：wait方法必须先获得锁
	 */
	public synchronized void waitForBuffing() throws InterruptedException {
		// 如果没有清洗，一直等待
		while (waxed) {
			this.wait();
		}
	}

	/**
	 * 等待打蜡完成，需要加锁：wait方法必须先获得锁
	 */
	public synchronized void waitForWaxing() throws InterruptedException {
		// 如果没有打蜡，一直等待
		while (!waxed) {
			this.wait();
		}
	}
}

// 开始打蜡任务
class WaxOn1 implements Runnable {
	private Car1 car;

	public WaxOn1(Car1 car) {
		this.car = car;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				System.out.print("WaxOn! ");
				// 模拟工作时间
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

// 打蜡完成，继续清洗任务
class WaxOff1 implements Runnable {
	private Car1 car;

	public WaxOff1(Car1 car) {
		this.car = car;
	}

	@Override
	public void run() {
		try {
			/*
			 * 注意这里的调用顺序，会依赖car的初始状态
			 */
			while (!Thread.interrupted()) {
				// 首先要等待打蜡完成，因为Car默认的waxed状态是false，表示等待打蜡
				this.car.waitForWaxing();
				System.out.print("WaxOff! ");
				TimeUnit.MILLISECONDS.sleep(100);
				// 更改状态
				this.car.buffed();
			}
		} catch (InterruptedException e) {
			System.out.print("WaxOff：任务中断！");
		}
	}
}