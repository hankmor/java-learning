package com.belonk.concurrent.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sun on 2017/3/5.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class LiftOff implements Runnable {
	//~ Static fields/initializers =====================================================================================
	private static int taskCount = 0;

	//~ Instance fields ================================================================================================
	protected int countDown = 10;
	private final int id = taskCount++;

	//~ Constructors ===================================================================================================
	public LiftOff() {
	}

	public LiftOff(int countDown) {
		this.countDown = countDown;
	}

	//~ Methods ========================================================================================================

	@Override
	public void run() {
		while (countDown-- > 0) {
			System.out.print(status());
			Thread.yield();
		}
	}

	protected int getId() {
		return id;
	}

	protected String status() {
		return "#" + id + "(" + (countDown > 0 ? countDown : "LiftOff!") + "), ";
	}

	public static void main(String[] args) throws InterruptedException {
//        LiftOff liftOff = new LiftOff();
//        liftOff.run();

		// Thread thread = new Thread(new LiftOff());
		// thread.start();
		// System.out.println("Waiting for LiftOff");

		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.execute(new LiftOff());
		executorService.execute(new LiftOff());
		executorService.execute(new LiftOff());
		executorService.shutdown();
	}
}
