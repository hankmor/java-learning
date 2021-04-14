package com.belonk.concurrent.thread.wait;

import com.belonk.concurrent.util.SleeperUtil;
import com.belonk.util.Printer;

/**
 * Created by sun on 2021/4/14.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class WaitNotify {
	//~ Static fields/constants/initializer


	//~ Instance fields

	// 是否被唤醒标记
	private static boolean flag = true;
	private final static Object lock = new Object();

	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		/*
		说明：
		1、线程对象存储在两个队列：同步队列、等待队列，前者用来存储BLOCKED状态的线程，可以竞争锁；而等待队列存储处于WAITING状态的线程，
		等待被唤醒，调用wait、notify会在这两个队列之间移动线程
		2、wait, notify, notifyAll方法调用时必须加锁
		3、wait调用后线程立即释放锁，状态有RUNNABLE变为WAITING，当前对象放入等待队列
		4、notify、notifyAll调用后，等待线程不会立即从wait方法返回，还需要等待notify方法调用线程释放锁，并且等待线程获得了锁才能从wait返回
		5、notify会随机唤醒用该锁的一个线程，将其从等待队列移动到同步队列中，而notifyAll会将该锁下的所有线程从等待队列移动到同步队列

		等待/通知的经典范式：
		1、等待
		synchronized (lock) {
			while (条件不满足) { // 只能用while判断条件，不能用if
				lock.wait();
			}
			对应处理逻辑
		}
		2、通知
		synchronized (lock) {
			改变条件
			lock.notifyAll();
		}
		 */

		new Thread(new Wait(), "wait").start();
		new Thread(new Notify(), "notify").start();

		/*
		输出：
		wait flag为 true, 等待
		notify 持有锁，准备唤醒所有线程
		wait flag为 false， 被唤醒
		notify 再次持有锁

		最后两句的顺序可能互换，由谁先抢到锁决定。
		 */
	}

	static class Wait implements Runnable {
		@Override
		public void run() {
			synchronized (lock) {
				// 注意：判断flag条件必须使用while，不能用if，否则会因虚假唤醒造成结果不正确
				// if (flag) { // 错误
				while (flag) {
					Printer.println(Thread.currentThread().getName() + " flag为 true, 等待");
					try {
						lock.wait(); // 释放锁，处于同步队列的notify线程会获得锁
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// 被唤醒了，打印这一句
				Printer.println(Thread.currentThread().getName() + " flag为 false， 被唤醒");
			}
		}
	}

	static class Notify implements Runnable {
		@Override
		public void run() {
			synchronized (lock) {
				Printer.println(Thread.currentThread().getName() + " 持有锁，准备唤醒所有线程");
				// 唤醒lock上的所有线程（其实就是唤醒wait线程，将其从等待队列移动到同步队列，让其参与竞争锁），让它们竞争锁，
				// 但是需要等待notify线程释放锁，此时wait线程才可以竞争锁
				lock.notifyAll();
				flag = false;
				SleeperUtil.seconds(1); // 睡眠，能更好的看清锁竞争导致的输出顺序不一致
			}
			// 竞争成功，会再次获得锁
			synchronized (lock) {
				Printer.println(Thread.currentThread().getName() + " 再次持有锁");
			}
		}
	}
}