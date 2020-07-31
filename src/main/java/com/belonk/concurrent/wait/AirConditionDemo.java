package com.belonk.concurrent.wait;

/**
 * Created by sun on 2020/7/31.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class AirConditionDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		AirConditionOperator operator = new AirConditionOperator();
		operator.operate();
	}
}

// 空调资源类
class AirCondition {
	// 温度
	private int temperature;

	// 升高温度，当温度不为0的时候，需要等待其他线程将温度变为0才能在升高
	// 必须加锁，才能保证wait、notifyAll方法使用同一个监视器
	public synchronized void temperatureUp() throws InterruptedException {
		// 假设温度不为0时，不能调高温度
		// 多线程的判断，存在虚假唤醒，所以必须使用while循环来判断条件，而不能用if判断
		// if (temperature != 0) {
		while (temperature != 0) {
			// 睡眠，等待被唤醒
			this.wait();
		}
		temperature++;
		System.out.println(Thread.currentThread().getName() + "\t当前温度: " + temperature);
		// 唤醒其他线程
		this.notifyAll();
	}

	// 升高温度，当温度为0的时候，需要等待其他线程将温度升高才能在升高
	// 必须加锁，才能保证wait、notifyAll方法使用同一个监视器
	public synchronized void temperatureDown() throws InterruptedException {
		// 假设温度为0时，不能再调低温度
		while (temperature == 0) {
			// 睡眠，等待被唤醒
			this.wait();
		}
		temperature--;
		System.out.println(Thread.currentThread().getName() + "\t当前温度: " + temperature);
		// 唤醒其他线程
		this.notifyAll();
	}
}

class AirConditionOperator {
	public void operate() {
		AirCondition airCondition = new AirCondition();

		// 多个线程操作空调
		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				try {
					airCondition.temperatureUp();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "up").start();

		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				try {
					airCondition.temperatureDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "down").start();

		// 再添加两个线程来操作空调，一共是4个线程

		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				try {
					airCondition.temperatureUp();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "up1").start();

		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				try {
					airCondition.temperatureDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "down1").start();
	}
}