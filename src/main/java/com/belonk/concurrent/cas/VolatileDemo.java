package com.belonk.concurrent.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发编程三大特性：
 * <ul>
 *     <li>原子性：一系列操作，要么全部执行完成，要么都不执行，不允许部分成功</li>
 *     <li>可见性：通知操作某一数据的多个线程，某线程修改了数据，其他线程能够立即看到修改后的值</li>
 *     <li>有序性：程序执行的顺序按照代码编写的先后顺序执行</li>
 * </ul>
 * JMM: java内存模型，每个线程都有自己的工作内存，首先会将操作数据从JVM内存(主内存)拷贝到自己的工作内存中，操作完成再写回主内存。
 * <p>
 * 由于在并发环境下，多个线程进行资源抢占，可能出现写覆盖，一个线程写入数据时将其他线程的值给覆盖了，比如：
 * <pre>
 * class UnsafeCounter {
 *     private int count;
 *
 *     // 两个线程a、b同时调用此方法，首先从主内存中拷贝count的值到自己的工作内存，比如此时的值为0；
 *     // 然后，a线程在自己的工作内容修改count的值为1并写入到主内存中，但是b线程并不知道a已经修改了count的值，b线程在自己的工作内容修改
 *     // count的值也为1，写入到主内存时，将a已经修改的值给覆盖掉了
 *     public void increment() {
 *         count++;
 *     }
 *
 *     public int getCount() {
 *         return count;
 *     }
 * }
 * </pre>
 * <p>
 * Volatile可以解决上述线程间数据修改后可见性的问题，它可以保证线程修改了共享变量的值会立即刷新到主内存，并通知其他线程该值已经更改，
 * 其他线程直接从主内存读取值。
 * <p>
 * Volatile是轻量级的同步机制，可以保证数据可见性、禁止指令重排（有序性），但是不能保证原子性。
 * <ul>
 * <li>可见性：某个线程修改了数据，其他线程能够立即看到修改后的值
 * <li>有序性：禁止jvm对执行代码进行指令重排序，按照代码编写顺序执行，见{@link InstructionReorderingDemo}。
 * </ul>
 * <p>
 * <p>
 * volatile底层的实现原理：https://mp.weixin.qq.com/s/keaORK_ePM3x2GLcrFZZHw
 * <p>
 * Created by sun on 2020/7/26.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class VolatileDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		// viewAllThread();

		// 无volatile，无法保证原子性
		// unsafeCounterTest();

		// 解决原子性的一种方案：使用重量级锁保证原子性，如synchronized或者Lock
		// safeCounterTest();

		// 有volatile，也无法保证原子性
		// unsafeCounterTest2();

		// volatile的可见性验证
		// visibilityTest();

		// 解决原子性的更优方案：使用JUC下的AtomicXXX类
		safeCounterTest2();
	}

	public static void viewAllThread() {
		// 默认有两个线程，main主线程和GC线程
		System.out.println("active thread: " + Thread.activeCount());
		Thread[] threads = new Thread[Thread.activeCount()];
		int enumerate = Thread.enumerate(threads);
		for (int i = 0; i < enumerate; i++) {
			Thread thread = threads[i];
			// main
			// Monitor Ctrl-Break
			System.out.println(thread.getName());
		}
	}

	public static void unsafeCounterTest() {
		UnsafeCounter unsafeCounter = new UnsafeCounter();
		// 10个线程，每个线程技术1000此，期望的结果是10000
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				for (int j = 0; j < 1000; j++) {
					unsafeCounter.increment();
				}
			}, "thread" + i).start();
		}
		// 查看最终计算结果是多少
		// 默认有两个线程，如果激活的线程数 > 2，说明有线程还没计算完成
		while (Thread.activeCount() > 2) {
			// 主线程休眠
			Thread.yield();
		}
		// 多次执行，可以看到结果小于10000，跟预期结果不一致
		System.out.println(unsafeCounter.getCount());
	}

	public static void safeCounterTest() {
		SafeCounter safeCounter = new SafeCounter();
		// 10个线程，每个线程技术1000此，期望的结果是10000
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				for (int j = 0; j < 1000; j++) {
					safeCounter.increment();
				}
			}, "thread" + i).start();
		}
		// 查看最终计算结果是多少
		// 默认有两个线程，如果激活的线程数 > 2，说明有线程还没计算完成
		while (Thread.activeCount() > 2) {
			// 主线程休眠
			Thread.yield();
		}
		// 多次执行，可以看到结果为预期的10000
		System.out.println(safeCounter.getCount());
	}

	public static void unsafeCounterTest2() {
		UnsafeCounter2 safeCounter = new UnsafeCounter2();
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				for (int j = 0; j < 1000; j++) {
					safeCounter.increment();
				}
			}, "thread" + i).start();
		}
		while (Thread.activeCount() > 2) {
			Thread.yield();
		}
		// 多次执行，可以看到结果小于10000，跟预期结果不一致
		// 说明：虽然加了volatile，但是结果仍然不一致，因为多线程调用increment方法，该方法并不满足原子调用（未加锁）
		// 间接证明volatile不能保证原子性
		System.out.println(safeCounter.getCount());
	}

	public static void visibilityTest() {
		Counter counter = new Counter();
		// 新线程修改了count的值
		new Thread(() -> {
			// 3秒钟之后，改为20
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			counter.changeTo20();
		}, "mythread").start();

		// 一直循环判断值是否为10，如果count属性没有加volatile，会一直死循环，其他线程修改了值而main线程不可见
		// 相反，如果加了volatile，mythread线程改了值，main线程立即能够获取到新值
		while (counter.getCount() == 10) {

		}
		// 初始值是10，如果未改变，那么新的值不能打印出来
		System.out.println("查看新的值：" + counter.getCount());
	}

	public static void safeCounterTest2() {
		SafeCounter2 safeCounter = new SafeCounter2();
		// 10个线程，每个线程技术1000此，期望的结果是10000
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				for (int j = 0; j < 1000; j++) {
					safeCounter.increment();
				}
			}, "thread" + i).start();
		}
		// 查看最终计算结果是多少
		// 默认有两个线程，如果激活的线程数 > 2，说明有线程还没计算完成
		while (Thread.activeCount() > 2) {
			// 主线程休眠
			Thread.yield();
		}
		// 多次执行，可以看到结果为预期的10000
		System.out.println(safeCounter.getCount());
	}
}

class UnsafeCounter {
	private int count;

	public void increment() {
		count++;
	}

	public int getCount() {
		return count;
	}
}

class SafeCounter {
	private int count;

	// 加锁，保证方法的原子性，但是使用的是重量级的方案：加锁，性能并不高
	public synchronized void increment() {
		count++;
	}

	public int getCount() {
		return count;
	}
}

class UnsafeCounter2 {
	// 添加了volatile关键字
	private volatile int count;

	// 方法调用不能保证原子操作，那么volatile关键字能否保证原子性？答案是否定的
	public void increment() {
		// 这行代码有三个操作：先从主内存拷贝值到工作内存，然后将其加1，最后，再写回主内存
		// 这三个操作并不是原子性的，可能产生写覆盖
		count++;
	}

	public int getCount() {
		return count;
	}
}

class Counter {
	// 添加了volatile关键字
	private volatile int count = 10;

	// 将count的值改为20
	public void changeTo20() {
		this.count = 20;
	}

	public int getCount() {
		return count;
	}
}

class SafeCounter2 {
	private final AtomicInteger counter = new AtomicInteger();

	public void increment() {
		counter.getAndIncrement();
	}

	public int getCount() {
		return counter.get();
	}
}