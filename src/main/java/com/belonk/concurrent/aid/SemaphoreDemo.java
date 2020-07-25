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


	public static void main(String[] args) {
		test1();
		// testPool();
		// testPark();
	}

	public static void test1() {
		int permits = 5;
		int acquirePermits = 2;
		Semaphore semaphore = new Semaphore(permits);
		for (int i = 0; i < permits * 3; i++) {
			new Thread(() -> {
				try {
					semaphore.acquire(acquirePermits);
					System.out.println(Thread.currentThread().getName() + "\t获得许可，可用许可数量：" + semaphore.availablePermits());
					try {
						TimeUnit.SECONDS.sleep(3);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					semaphore.release(acquirePermits);
					System.out.println(Thread.currentThread().getName() + "\t释放许可，可用许可数量：" + semaphore.availablePermits());
				}
			}, "t" + i).start();
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
class Park {
	// 车位数量
	private final int stallNumber;

	private Semaphore semaphore;

	public Park(int stallNumber) {
		System.out.println("车场车位数量：" + stallNumber);
		this.stallNumber = stallNumber;
		// 两个参数:
		// permits: 许可数量，可能为负数，表示：许可释放必须先于获取
		// fair: 是否公平竞争，true：公平，即等待时间越久的线程越先获取许可，false：非公平，默认为false
		// 默认为false，车辆抢占车位顺序是无序的
		this.semaphore = new Semaphore(stallNumber);
		// 设置为true，可以看到车辆按顺序抢占车位
		// this.semaphore = new Semaphore(stallNumber, true);
	}

	public int getStallNumber() {
		return stallNumber;
	}

	// 停车
	public void parking() {
		try {
			// 获取许可，如果不能获取就阻塞
			System.out.println(Thread.currentThread().getName() + "\t正在等待车位...");
			semaphore.acquire();
			// TODO 这里的空余车位：semaphore.availablePermits()，为什么会出现错误如下：
			// car0	抢到车位，当前空余车位：8
			// car1	抢到车位，当前空余车位：8
			// 这里的car0和car1抢车位，车位剩余都是8？？
			System.out.println(Thread.currentThread().getName() + "\t抢到车位，当前空余车位：" + semaphore.availablePermits());
			// if (semaphore.availablePermits() == 0) {
			// 	System.out.println("====车位已满====");
			// }
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