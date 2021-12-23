package com.belonk.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/12/23.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class InterruptIdiom {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 线程中断示例：该示例演示了正确清理资源的重要性，无论线程是否被中断，都应该正确清理资源
	 */

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newCachedThreadPool();

		// 通过睡眠时间来控制退出线程 run 方法的位置，是在阻塞的sleep时由于线程中断退出，还是在耗时的计算操作完成后退出
		// 1、如果实在阻塞的 sleep 处(标记<1>) 中断退出，那么由于会抛出 InterruptException 会打印异常信息，仍然能够正确清理资源
		// 2、如果在耗时的计算操作时（标记<2>）处退出，不会抛出异常，程序正常退出

		executorService.execute(new Blocked());
		// sleep时中段，抛出异常，仍然能够清理资源
		TimeUnit.MILLISECONDS.sleep(300);
		executorService.shutdownNow();

		TimeUnit.SECONDS.sleep(2);
		System.out.println();

		executorService = Executors.newCachedThreadPool();
		executorService.execute(new Blocked());
		// 程序正确退出，能够正常清理资源
		TimeUnit.MILLISECONDS.sleep(1010);
		executorService.shutdownNow();

		/*
		// 异常退出，但正确清理资源
		NeedCleanup 1
		Sleeping...
		Cleanup 1
		InterruptedException

		// 正常退出，正确清理资源
		NeedCleanup 1
		Sleeping...
		NeedCleanup 2
		Expensive calculating...
		Expensive calculate finished
		Cleanup 2
		Cleanup 1
		Exit while
		 */
	}
}

/**
 * 需要被清理的对象
 */
class NeedCleanup {
	private final int id;

	public NeedCleanup(int id) {
		this.id = id;
		System.out.println("NeedCleanup " + this.id);
	}

	/**
	 * 清理资源
	 */
	public void cleanup() {
		System.out.println("Cleanup " + this.id);
	}
}

class Blocked implements Runnable {
	private double d = 0.0d;

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				NeedCleanup nc1 = new NeedCleanup(1);
				try {
					// 阻塞操作
					System.out.println("Sleeping...");
					TimeUnit.SECONDS.sleep(1); // <1>

					NeedCleanup nc2 = new NeedCleanup(2);
					try {
						// 耗时的计算，非阻塞操作
						System.out.println("Expensive calculating...");
						for (int i = 0; i < 300_0000; i++) { // <2>
							d = d + (Math.PI + Math.E) / d;
						}
						System.out.println("Expensive calculate finished");
					} finally {
						nc2.cleanup();
					}
				} finally {
					nc1.cleanup();
				}
			}
			System.out.println("Exit while");
		} catch (InterruptedException e) {
			System.out.println("InterruptedException");
		}
	}
}