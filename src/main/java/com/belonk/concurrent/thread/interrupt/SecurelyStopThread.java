package com.belonk.concurrent.thread.interrupt;

import com.belonk.concurrent.util.SleeperUtil;
import com.belonk.util.Printer;

/**
 * Created by sun on 2021/4/14.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class SecurelyStopThread {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		/*
		停止线程，安全的方式：
		1、通过检查线程自带的中断标志位(thread.interrupt() 和 isInterrupted()方法)，来设定线程终止条件
		2、自定义一个中断标记，需要 申明为volatile，这样线程之间都可以看到其改变，然后检查这个标记来终止线程
		 */
		Thread thread1 = new Thread(new StopThread(), "interrupt-thread");
		thread1.start();
		SleeperUtil.seconds(2);
		thread1.interrupt(); // 设置中断标记，线程退出

		StopThread stopThread = new StopThread();
		Thread thread2 = new Thread(stopThread, "custom-tag-thread");
		thread2.start();
		SleeperUtil.seconds(2);
		stopThread.cancel(); // 设置自定义标记状态为false，线程退出

		/*
		可能的输出：

		interrupt-thread i = 1818069582
		custom-tag-thread i = 1842165924
		 */
	}
}

class StopThread implements Runnable {
	private int i;
	private volatile boolean on = true;

	@Override
	public void run() {
		while (on && !Thread.currentThread().isInterrupted()) { // 通过自定义的标记开关和线程的中断状态标记来作为判断终止条件
			i++;
		}
		// 打印输出
		Printer.println(Thread.currentThread().getName() + " i = " + i);
	}

	public void cancel() {
		this.on = false;
	}
}