package com.belonk.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 装饰花园的一个仿真程序，收集花园的每个入口进入的人数，然后统计总人数。
 * <p>
 * Created by sun on 2021/12/18.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class OrnamentalGarden {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*

	 */

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newCachedThreadPool();
		// 5个线程同时执行，相同于一共有5个入口
		for (int i = 0; i < 5; i++) {
			executorService.execute(new Entrance(i));
		}
		// executorService.execute(() -> {
		// 	while (!executorService.isShutdown()) {
		// 		System.out.println("=== Total: " + Entrance.getTotalCount());
		// 		System.out.println("=== Sum Entrances: " + Entrance.sumEntrances());
		// 		try {
		// 			TimeUnit.MILLISECONDS.sleep(500);
		// 		} catch (InterruptedException e) {
		// 			e.printStackTrace();
		// 		}
		// 	}
		// });
		// 充分执行3秒
		TimeUnit.SECONDS.sleep(3);
		// 关闭所有入口
		Entrance.cancel();
		// 关闭线程池
		executorService.shutdown();

		// 阻塞，等待任务结束，直到到达超时时间
		// 将睡眠时间该小，观察结果，可以看到，虽然有任务没有结束，但是同样可以正确统计总数量
		if (!executorService.awaitTermination(250, TimeUnit.MILLISECONDS)) {
			System.out.println("Some task were not terminated.");
		}

		System.out.println("Total: " + Entrance.getTotalCount());
		System.out.println("Sum Entrances: " + Entrance.sumEntrances());
	}
}

/**
 * 计数器，用来统计所有入口的进入总人数
 */
class Count {
	private int count = 0;
	private static final Random random = new Random(47);

	// 数量增加1，需要同步来保证不会重复
	public synchronized int increment() {
		// 故意创建临时变量，先符赋值后加，中间随机让出CPU
		int temp = count;
		// 随机让出CPU
		if (random.nextBoolean()) {
			Thread.yield();
		}
		// temp加1然后赋值给count，并返回count当前值
		return count = ++temp;
	}

	// 总的人数
	public synchronized int value() {
		return count;
	}
}

/**
 * 花园的入口
 */
class Entrance implements Runnable {
	// 计数器，统计总人数
	private static final Count count = new Count();
	// 入口的引用列表，便于统计每个入口分别的进入人数
	private static final List<Entrance> entrances = new ArrayList<>();
	// 每个入口的单独人数统计变量
	private int number;
	// 入口编号
	private final int id;
	// 是否取消标志
	private static volatile boolean canceled = false;

	public Entrance(int id) {
		this.id = id;
		// 加入列表
		entrances.add(this);
	}

	// 取消，即：关闭所有入口
	public static void cancel() {
		canceled = true;
	}

	@Override
	public void run() {
		while (!canceled) {
			// 本身的数量增加，多个线程同时写入，需要同步
			synchronized (this) {
				number++;
			}

			// 打印当前入口的数量和总的数量，计数器的数量增加
			System.out.println(this + " Total " + count.increment());

			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 停止
		System.out.println("Stopping " + this);
	}

	// 本入口的人数，多个线程同时读取，需要同步
	public synchronized int getValue() {
		return number;
	}

	// 当前总人数
	public static int getTotalCount() {
		return count.value();
	}

	// 当前总人数：统计每个入口的人数之和
	public static int sumEntrances() {
		int cnt = 0;
		for (Entrance entrance : entrances) {
			cnt += entrance.getValue();
		}
		return cnt;
	}

	@Override
	public String toString() {
		return "Entrance " + id + ": " + this.getValue();
	}
}