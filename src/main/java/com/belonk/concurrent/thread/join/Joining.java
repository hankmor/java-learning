package com.belonk.concurrent.thread.join;

import com.belonk.util.Printer;

class Sleepper extends Thread {
	private int sleepTime;

	public Sleepper(String name, int sleepTime) {
		super(name);
		this.sleepTime = sleepTime;
		start();
	}

	@Override
	public void run() {
		try {
			sleep(sleepTime);
		} catch (InterruptedException e) {
			Printer.println(getName() + " was interrupted, isInterrupted : " + isInterrupted());
			return; // 中断过后直接返回
		}
		// 睡眠时间到后唤醒
		Printer.println(getName() + " has awakened.");
	}
}

class Joiner extends Thread {
	private Sleepper sleepper;

	public Joiner(String name, Sleepper sleepper) {
		super(name);
		this.sleepper = sleepper;
		start();
	}

	@Override
	public void run() {
		try {
			sleepper.join();
		} catch (InterruptedException e) {
			Printer.println(sleepper.getName() + " interrupted.");
		}
		Printer.println(getName() + " join completed.");
	}
}

/**
 * Created by sun on 2017/3/6.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class Joining {
	//~ Static fields/initializers =====================================================================================


	//~ Instance fields ================================================================================================


	//~ Constructors ===================================================================================================


	//~ Methods ========================================================================================================
	public static void main(String[] args) {
		Sleepper sleepperA = new Sleepper("sleeperA", 5000);
		Sleepper sleepperB = new Sleepper("sleeperB", 5000);
		Joiner joinerA = new Joiner("joinerA", sleepperA);
		Joiner joinerB = new Joiner("joinerB", sleepperB);
		sleepperB.interrupt(); // 中断join方法
	}
    /*
     sleeperB was interrupted, isInterrupted : false
     joinerB join completed.
     sleeperA has awakened.
     joinerA join completed.
     */
}
