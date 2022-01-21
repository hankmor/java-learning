package com.belonk.concurrent.aid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by sun on 2022/1/21.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class HorseRace {
	//~ Static fields/constants/initializer

	private static final ExecutorService executorService = Executors.newCachedThreadPool();
	// 参赛的马匹
	private static final List<Horse> raceHorses = new ArrayList<>();

	//~ Instance fields

	// 参赛的马匹数
	private int horseNumber = 5;
	// 赛道的步数，马匹跑多少不完成比赛
	private int lineNumber = 100;

	//~ Constructors

	public HorseRace() {
	}

	public HorseRace(int horseNumber, int lineNumber) {
		this.horseNumber = horseNumber;
		this.lineNumber = lineNumber;
	}

	//~ Methods

	/*
	 * 基于 CyclicBarrier 的赛马仿真程序，来源《Java编程思想》
	 */

	public HorseRace ready() {
		System.out.println("Ready! " + horseNumber + " horses will run " + lineNumber + " to win the race.");
		CyclicBarrier cyclicBarrier = new CyclicBarrier(this.horseNumber, () -> {
			// 打印赛道
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < lineNumber; i++) {
				builder.append("=");
			}
			System.out.println(builder);
			// 打印马的奔跑路径
			for (Horse horse : raceHorses) {
				System.out.println(horse.getTracks());
				if (horse.getStride() >= this.lineNumber) {
					System.out.println(horse + " won the race!");
					executorService.shutdownNow();
					return;
				}
			}
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		for (int i = 0; i < horseNumber; i++) {
			raceHorses.add(new Horse(cyclicBarrier));
		}
		return this;
	}

	public void go() {
		try {
			for (int i = 3; i > 0; i--) {
				System.out.println(i + " seconds left.");
				TimeUnit.SECONDS.sleep(1);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		System.out.println("Go!!!");
		for (Horse horse : raceHorses) {
			executorService.execute(horse);
		}
	}

	public static void main(String[] args) {
		new HorseRace().ready().go();
	}
}

// 马
class Horse implements Runnable {
	// 给每匹马一个id
	private static int number = 0;
	private final int id = number++;
	// 随机数
	private static final Random random = new Random(47);
	// 跑的步数
	private volatile int stride;
	// 表示终点的位置
	private final CyclicBarrier barrier;

	public Horse(CyclicBarrier barrier) {
		this.barrier = barrier;
		this.show();
	}

	// 出场
	private void show() {
		System.out.println("Horse " + id + " is coming to the start line...");
	}

	// 开始跑起来
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized (this) {
					// 模拟跑的步频的快慢
					stride += random.nextInt(3);
				}
				barrier.await();
			}
		} catch (InterruptedException e) {
			System.out.println("比赛中断！");
		} catch (BrokenBarrierException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "Horse " + id;
	}

	/**
	 * 获取跑的步数，多个马都在跑，需要同步
	 */
	public synchronized int getStride() {
		return stride;
	}

	/**
	 * 模拟马匹跑的轨迹
	 */
	public String getTracks() {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < getStride(); i++) {
			b.append("*");
		}
		b.append(id);
		return b.toString();
	}
}
