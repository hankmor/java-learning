package com.belonk.concurrent.thread.deamon;

import com.belonk.util.Printer;

/**
 * 大量生产线程
 */
class DaemonSpawn implements Runnable {
	@Override
	public void run() {
		while (true) {
			Thread.yield();
		}
	}
}

/**
 * 产生更多线程
 */
class Daemon implements Runnable {
	private Thread[] threads = new Thread[10];

	@Override
	public void run() {
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new DaemonSpawn());
			threads[i].start();
			Printer.println("DaemonSpawn " + i + " started.");
		}
		for (int i = 0; i < threads.length; i++) {
			Printer.println("threads[" + i + "].isDaemon() = " + threads[i].isDaemon());
		}
		while (true) {
			Thread.yield();
		}
	}
}

/**
 * Created by sun on 2017/3/5.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class Daemons {
	//~ Static fields/initializers =====================================================================================


	//~ Instance fields ================================================================================================


	//~ Constructors ===================================================================================================


	//~ Methods ========================================================================================================

	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(new Daemon());
		thread.setDaemon(true);
		thread.start();
		Printer.println("concurrent.isDaemon() = " + thread.isDaemon());
		Thread.sleep(10);
	}
}
