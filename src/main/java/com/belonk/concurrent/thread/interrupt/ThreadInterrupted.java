package com.belonk.concurrent.thread.interrupt;

import com.belonk.util.PrintHelper;

import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/4/13.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ThreadInterrupted {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws InterruptedException {
		/*
		线程中断：线程中断其实就是在线程中设置一个标志位(interrupted status)，通过检查该标志位来决定线程执行的逻辑
		1、抛出InterruptedException的方法（Object的wait, Thread的sleep、join等），被中断时会首先清除中断标记，然后抛出InterruptedException异常，结果就是
		即是说isInterrupted()返回false
		2、interrupt()方法只是设置了线程的中断标记位，并不是真正的将线程中段，所以可以看到busier线程即是中断了也一直在运行
		3、线程退出过后(TERMINATED状态)，即使之前被中断过(调用interrupt()方法)，但是isInterrupted()仍然返回false。
		4、调用Thread.interrupted静态方法时，会检查当前线程是否处于中断状态，并清除中断标记；而isInterrupted()不会清除中断标记

		取消Busier的<1>注释看看执行结果？
		 */
		Thread finished = new Thread(new Finished(), "finished");
		Thread sleeper = new Thread(new Sleeper(), "sleeper");
		Thread busier = new Thread(new Busier(), "busier");
		Thread clearInterrupteStatus = new Thread(new ClearInterrupteStatus(), "clearInterrupteStatus");
		finished.start();
		sleeper.start();
		busier.start();
		clearInterrupteStatus.start();
		TimeUnit.SECONDS.sleep(3);
		System.out.println("before interrupt state -> finished: " + finished.getState());
		System.out.println("before interrupt state -> sleeper: " + sleeper.getState());
		System.out.println("before interrupt state -> busier: " + busier.getState());
		System.out.println("before interrupt state -> clearInterrupteStatus: " + clearInterrupteStatus.getState());
		finished.interrupt();
		sleeper.interrupt(); // 睡眠中的线程，被Main线程中断，抛出InterruptedException
		busier.interrupt();
		clearInterrupteStatus.interrupt();
		PrintHelper.println(finished.getName() + " interrupted: " + finished.isInterrupted()); // false，线程终结，即是之前被中断也返回false
		PrintHelper.println(sleeper.getName() + " interrupted: " + sleeper.isInterrupted()); // false，说明中断标记被清除
		PrintHelper.println(busier.getName() + " interrupted: " + busier.isInterrupted()); // true，线程被中断，只能说明中断标记设置了，但是线程仍然在运行(因为while(true))
		PrintHelper.println(clearInterrupteStatus.getName() + " interrupted: " + clearInterrupteStatus.isInterrupted()); // false，说明中断标记被清除
		System.out.println("after interrupt state -> finished: " + finished.getState());
		System.out.println("after interrupt state -> sleeper : " + sleeper.getState());
		System.out.println("after interrupt state -> busier : " + busier.getState());
		System.out.println("after interrupt state -> clearInterrupteStatus : " + clearInterrupteStatus.getState());

		/*
		before interrupt state -> finished: TERMINATED
		before interrupt state -> sleeper: TIMED_WAITING
		before interrupt state -> busier: RUNNABLE
		before interrupt state -> clearInterrupteStatus: RUNNABLE
		after interrupted running...
		finished interrupted: false
		sleeper interrupted: false
		busier interrupted: true
		clearInterrupteStatus interrupted: false
		after interrupt state -> finished: TERMINATED
		after interrupt state -> sleeper : RUNNABLE
		after interrupt state -> busier : RUNNABLE
		after interrupt state -> clearInterrupteStatus : RUNNABLE
		java.lang.InterruptedException: sleep interrupted
			at java.lang.Thread.sleep(Native Method)
			at java.lang.Thread.sleep(Thread.java:340)
			at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
			at com.belonk.concurrent.thread.interrupt.Sleeper.run(ThreadInterrupted.java:95)
			at java.lang.Thread.run(Thread.java:748)
		 */
	}
}

class Finished implements Runnable {
	@Override
	public void run() {
	}
}

// 一直睡眠
class Sleeper implements Runnable {
	@Override
	public void run() {
		while (true) {
			// while (!Thread.currentThread().isInterrupted()) {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

// 一直运行
class Busier implements Runnable {
	@Override
	public void run() {
		while (true) { // 一直运行，不检查中段标记位
			// while (!Thread.currentThread().isInterrupted()) { // <1> 检查中段标记位，然后线程退出，线程处于 TERMINATED 状态
			// System.out.println("running");
		}
	}
}

// 清除中断标记线程
class ClearInterrupteStatus implements Runnable {
	@Override
	public void run() {
		while (true) {
			if (Thread.interrupted()) {
				// 调用interrupted()方法后，线程中断，然后再调用Thread.interrupted()返回true并且又清除中断标记
				// 所以下边的打印只会输出一次
				System.out.println("after interrupted running...");
			}
		}
	}
}