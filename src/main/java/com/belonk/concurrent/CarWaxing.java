package com.belonk.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/12/28.
 *
 * @author sunfuchang03@126.com
 * @see CarWaxing2 改进后的案例
 * @since 1.0
 */
public class CarWaxing {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 多线程的应用：给汽车打蜡，假设清洗汽车和打蜡是两个同时进行的任务（两个线程），它们之间操作同一辆汽车，以此来学习线程间的通信。
	 * 操作步骤：
	 * 1、清洗汽车（buff）
	 * 2、上蜡（wax）
	 * 3、重复清洗、打蜡的过程
	 */

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newCachedThreadPool();
		// 操作同一辆汽车
		Car car = new Car();
		executorService.execute(new WaxOn(car));
		executorService.execute(new WaxOff(car));

		// 3秒后停止
		TimeUnit.SECONDS.sleep(2);
		executorService.shutdownNow();

		/*
		虽然结果正确，但是打蜡和清洗的工作在Car上完成，这不合理，应该交给具体的任务来完成。

		 正在打蜡...
		 打蜡完成，可以清洗了
		 正在清洗...
		 清洗完成，可以打蜡了
		 正在打蜡...
		 打蜡完成，可以清洗了
		 正在清洗...
		 清洗完成，可以打蜡了
		 正在打蜡...
		 ...
		 */
	}
}

// 线程操作的资源：汽车
class Car {
	// 给定一个已经打蜡的标记，waxed为true，说明已打蜡，待清洗；waxed为false，说明已清洗，等待打蜡
	private boolean waxed = false;

	// 必须加锁，包装两个方法只能同时有一个执行
	public synchronized void buffed() throws InterruptedException {
		// 如果没有打蜡，一直等待
		while (!waxed) {
			this.wait();
		}
		System.out.println("正在清洗...");
		TimeUnit.MILLISECONDS.sleep(100);
		this.waxed = false;
		System.out.println("清洗完成，可以打蜡了");
		// 通知清洗完成，可以（继续）打蜡了
		this.notifyAll();
	}

	// 必须加锁，包装两个方法只能同时有一个执行
	public synchronized void waxed() throws InterruptedException {
		while (this.waxed) {
			this.wait();
		}
		System.out.println("正在打蜡...");
		TimeUnit.MILLISECONDS.sleep(100);
		this.waxed = true;
		System.out.println("打蜡完成，可以清洗了");
		// 打蜡完成，通知可以清洗了
		this.notifyAll();
	}
}

// 开始打蜡任务
class WaxOn implements Runnable {
	private Car car;

	public WaxOn(Car car) {
		this.car = car;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				this.car.waxed();
			}
		} catch (InterruptedException e) {
			System.out.println("WaxOn：任务中断");
		}
	}
}

// 打蜡完成，继续清洗任务
class WaxOff implements Runnable {
	private Car car;

	public WaxOff(Car car) {
		this.car = car;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				this.car.buffed();
			}
		} catch (InterruptedException e) {
			System.out.println("WaxOff：任务中断");
		}
	}
}