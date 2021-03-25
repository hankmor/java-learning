package com.belonk.concurrent.aid;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2020/7/24.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class SemaphoreDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	// 信号量：控制许可数量，持有一个许可总数，并设置每次允许执行的许可数量。常用来控制允许执行的线程数量
	public static void main(String[] args) {
		// test1();
		// testPool();
		// testParkWrong(); // 逻辑错误的示例：没有同步加锁，导致数据错误（剩余车位数量不正确）
		testPark(); // 正确示例
	}

	public static void test1() {
		int permits = 6;
		int acquirePermits = 2;
		// permit：初始总数量
		Semaphore semaphore = new Semaphore(permits);
		for (int i = 0; i < permits * 2; i++) {
			new Thread(() -> {
				try {
					int availablePermits = semaphore.availablePermits();
					if (availablePermits == 0) {
						System.out.println(Thread.currentThread().getName() + "\t没有可用的许可数量，需要阻塞等待其他线程释放许可.");
					}
					// 获取许可数量：acquirePermits，被允许的许可数量。如果没有可用的数量，则阻塞，直到线程被中断
					semaphore.acquire(acquirePermits);
					System.out.println(Thread.currentThread().getName() + "\t【获得】许可，可用许可数量：" + semaphore.availablePermits());
					try {
						TimeUnit.SECONDS.sleep(3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					// 释放许可数量，放回semaphore中
					semaphore.release(acquirePermits);
					System.out.println(Thread.currentThread().getName() + "\t【释放】许可，可用许可数量：" + semaphore.availablePermits());
				}
			}, "t" + i).start();
		}
	}

	public static void testParkWrong() {
		int stallNumber = 10;
		int cars = 20;
		ParkWrong parkWrong = new ParkWrong(stallNumber);
		for (int i = 0; i < cars; i++) {
			new Thread(parkWrong::parking, "car" + i).start();
		}
	}

	public static void testPark() {
		int stallNumber = 10;
		int cars = 20;
		Park park = new Park(stallNumber);
		for (int i = 0; i < cars; i++) {
			new Thread(park::parking, "car" + i).start();
		}
	}

	public static void testPool() {
		Pool pool = new Pool();
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				try {
					Object item = pool.getItem();
					try {
						TimeUnit.SECONDS.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + " -> " + item + "\t使用完成");
					pool.putItem(item);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}, "th" + i).start();
		}
	}
}

class Pool {
	private static final int MAX_AVAILABLE = 2;
	private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

	// Not a particularly efficient data structure; just for demo
	protected Object[] items = new Object[]{"a", "b"};
	protected boolean[] used = new boolean[MAX_AVAILABLE];

	public Object getItem() throws InterruptedException {
		available.acquire();
		System.out.println("获取后当前可用许可：" + available.availablePermits());
		return getNextAvailableItem();
	}

	public void putItem(Object x) {
		if (markAsUnused(x)) {
			available.release();
			System.out.println("释放后当前可用许可：" + available.availablePermits());
		}
	}

	/**
	 * 按顺序获取一个可用元素。
	 *
	 * @return 可用元素，如果没有可用元素则返回null
	 */
	protected synchronized Object getNextAvailableItem() {
		for (int i = 0; i < MAX_AVAILABLE; i++) {
			if (!used[i]) {
				used[i] = true;
				return items[i];
			}
		}
		// 都在使用，没有可用元素，返回null
		return null; // not reached
	}

	/**
	 * 标记元素为可用。
	 *
	 * @param item 元素
	 * @return 如果可用则返回true，否则返回false
	 */
	protected synchronized boolean markAsUnused(Object item) {
		for (int i = 0; i < MAX_AVAILABLE; ++i) {
			if (item == items[i]) {
				if (used[i]) {
					used[i] = false;
					return true;
				} else
					return false;
			}
		}
		return false;
	}
}

// 停车场
class ParkWrong {
	// 车位数量
	private final int stallNumber;
	private final Semaphore semaphore;

	public ParkWrong(int stallNumber) {
		this.stallNumber = stallNumber;
		System.out.println("车场车位数量：" + this.stallNumber);
		// 两个参数:
		// permits: 许可数量，可能为负数，表示：许可释放必须先于获取
		// fair: 是否公平竞争，true：公平，即等待时间越久的线程越先获取许可，false：非公平，默认为false
		// 默认为false，车辆抢占车位顺序是无序的
		this.semaphore = new Semaphore(stallNumber, false);
		// 设置为true，可以看到车辆按顺序抢占车位
		// this.semaphore = new Semaphore(stallNumber, true);
	}

	// 停车
	public void parking() {
		try {
			// 获取许可，如果不能获取就阻塞
			System.out.println(Thread.currentThread().getName() + "\t正在等待车位...");
			semaphore.acquire();
			// 这里的空余车位：semaphore.availablePermits()，为什么会出现错误如下：
			// car0	抢到车位，当前空余车位：8
			// car1	抢到车位，当前空余车位：8
			// 这里的car0和car1抢车位，车位剩余都是8？？
			// ！！原因：Semaphore acquire，release方法并不能保证原子性，不持有任何同步锁，需要业务本身自己维护同步锁。
			System.out.println(Thread.currentThread().getName() + "\t抢到车位，当前空余车位：" + semaphore.availablePermits());
			if (semaphore.availablePermits() == 0) {
				System.out.println("====车位已满====");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			long time = (long) (Math.random() * 10 + 2);
			try {
				TimeUnit.SECONDS.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 离场
			semaphore.release();
			System.out.println(Thread.currentThread().getName() + "\t离开了车位, 停车时间" + time + "秒, 当前空余车位：" + semaphore.availablePermits());
		}
	}
}

class Park {
	// 初始空余车位数量
	private volatile int stallNumber;
	// 停车数控制，假设每次只能停一辆车
	private final Semaphore semaphore;
	private static final Object enterLock = new Object();
	private static final Object leaveLock = new Object();

	public Park(int stallNumber) {
		this.stallNumber = stallNumber;
		System.out.println("车场当前空余车位数量：" + this.stallNumber);
		// 公平排队等待车位
		this.semaphore = new Semaphore(stallNumber, false);
	}

	// 入场停车
	public void enter() throws InterruptedException {
		// 假设，每次只能停一辆车，默认参数是1
		semaphore.acquire();
		synchronized (enterLock) {
			stallNumber--;
			if (stallNumber == 0) {
				System.out.println(Thread.currentThread().getName() + "\t抢到车位，当前没有空余车位");
			} else {
				System.out.println(Thread.currentThread().getName() + "\t抢到车位，当前空余车位：" + stallNumber);
			}
		}
	}

	// 离场出车
	public void leave() {
		// 模拟停车时间
		long time = (long) (Math.random() * 10 + 2);
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 加锁
		synchronized (leaveLock) {
			stallNumber++;
			System.out.println(Thread.currentThread().getName() + "\t离开了车位, 停车时间" + time + "秒, 当前空余车位：" + this.stallNumber);
		}
		semaphore.release();
	}

	// 停车
	public void parking() {
		try {
			// 入场
			enter();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			// 离场
			leave();
		}
	}
}