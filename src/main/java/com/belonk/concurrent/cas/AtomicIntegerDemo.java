package com.belonk.concurrent.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicInteger保证原子性原理：CAS
 * <p>
 * CAS(CompareAndSwap)：比较并交换，底层使用UnSafe类来实现：
 * <pre>
 * public class AtomicInteger {
 *     // 核心的Unsafe类，使用CAS操作保证原子性
 *     Unsafe unsafe = Unsafe.getUnsafe();
 *     private static final long valueOffset;
 *
 *     // 获取value属性的内存地址偏移位置
 *     static {
 *         try {
 *             valueOffset = unsafe.objectFieldOffset
 *             (AtomicInteger.class.getDeclaredField("value"));
 *         } catch (Exception ex) { throw new Error(ex); }
 *     }
 *     // volatile保证可见性
 *     private volatile int value;
 *
 *     public final int getAndIncrement() {
 *         // this为当前对象，valueOffset为对象属性在内存中的偏移位置, 1为增加的值
 *         return unsafe.getAndAddInt(this, valueOffset, 1);
 *     }
 * }
 * </pre>
 * 上边代码的valueOffset为对象属性的内存地址偏移量，AtomicInteger获取的是其value属性。
 *
 * <p>
 * 底层的Unsafe类拥有大量的native方法，包含了CAS的实现，比如调用的getAndAddInt方法：
 * <pre>
 *     // var1: 操作的对象
 *     // var2：操作对象的属性的内存位置偏移量，用于获取该属性的值
 *     // var3：添加的值
 *     public final int getAndAddInt(Object var1, long var2, int var4) {
 *         // 当前获取到的值
 *         int var5;
 *         do {
 *             // 从对象上获取属性的值
 *             var5 = this.getIntVolatile(var1, var2);
 *             // compareAndSwapInt：一个native方法，保证了原子性操作（通过CPU的原子指令（cmpxchg指令）保证原子性）
 *             // 比较并交换，如果var1对象上var2位置属性的值为var5，那么比较成功，将字段的值设置为var5+var4，返回true
 *             // 否则比较失败，返回false，然后进入循环一直比较，直到比较成功
 *         } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));
 *
 *         return var5;
 *     }
 * </pre>
 * <p>
 * <p>
 * 优缺点：
 * 优点：不用加锁就保证了原子性，提高了并发量
 * 缺点：
 * 1、循环时间长开销大：采用循环的方式一直进行CAS，在大量并发的情况下，可能造成某些线程一直进行循环，CPU使用率升高
 * 2、只能保证一个共享变量的原子性，多行代码时还是需要加锁
 * 3、ABA问题：线程间共享变量v的初始值为A，然后线程1修改为B，再改回为A，其他线程看到的值还是A，以为没有被修改过，还是可以
 * CAS成功，但是其实这个值已经是被修改过的了。解决：{@link ABADemo}
 * <p>
 * <p>
 * Created by sun on 2020/7/27.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class AtomicIntegerDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) throws NoSuchFieldException {
		atomicCounterTest();
	}

	public static void atomicCounterTest() {
		AtomicCounter counter = new AtomicCounter();
		// 10个线程，每个线程技术1000此，期望的结果是10000
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				for (int j = 0; j < 1000; j++) {
					counter.increment();
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
		System.out.println(counter.getCount());
	}
}

class AtomicCounter {
	// 使用JUC的AtomicInteger类实现计数器
	private AtomicInteger counter = new AtomicInteger();

	public void increment() {
		counter.getAndIncrement();
		// CAS: 比对期望值expect，相同则更新为update值并返回true，否则更新失败，返回false
		// counter.compareAndSet(expect, update)
	}

	public int getCount() {
		return counter.get();
	}
}