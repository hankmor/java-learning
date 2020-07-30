package com.belonk.concurrent.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Created by sun on 2020/7/30.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ABADemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		showABA();
		resolveABA();
	}

	/**
	 * 展示ABA问题
	 */
	static void showABA() {
		AtomicInteger integer = new AtomicInteger(100);
		new Thread(() -> {
			// 先改为200
			boolean b = integer.compareAndSet(100, 200);
			System.out.println(Thread.currentThread().getName() + "将值改为200，成功：" + b);
			// 再改回原来的100
			b = integer.compareAndSet(200, 100);
			System.out.println(Thread.currentThread().getName() + "将值改回原来的100，成功：" + b);
		}, "show aba thread1").start();

		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 1秒后，线程2想要从100改为300，其实这个100已经是被线程1改过的了，但是线程2仍然能够修改成功
		new Thread(() -> {
			boolean b = integer.compareAndSet(100, 300);
			// true，仍然能修改
			System.out.println(Thread.currentThread().getName() + "想要改为300，成功：" + b);
		}, "show aba thread2").start();
	}

	/**
	 * 解决ABA问题，使用{@link java.util.concurrent.atomic.AtomicStampedReference}类，自带一个版本
	 * 号标记，每修改一次，版本号加1，CAS时不仅要比较原值，还要比较版本号是否相同
	 */
	static void resolveABA() {
		// 初始值为100，版本号为1
		AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(100, 1);
		new Thread(() -> {
			// 先改为200
			/* 四个参数：
			 * expectedReference: 期望的值，用来比较
			 * newReference: 更新的新值
			 * expectedStamp: 期望的原始标记（版本号）
			 * newStamp: 更新的新版本号
			 */
			System.out.println(Thread.currentThread().getName() + "修改前版本号：" + stampedReference.getStamp());
			System.out.println(Thread.currentThread().getName() + "修改前值：" + stampedReference.getReference());
			// 不要使用compareAndSet(100,200,...)，虽然第一次可以，但是后续再次执行会新建Pair对象并与持有的pir进行CAS, 它们是不同的对象，造成更新失败
			boolean b = stampedReference.compareAndSet(stampedReference.getReference(), 200, stampedReference.getStamp(), stampedReference.getStamp() + 1);
			System.out.println(Thread.currentThread().getName() + "将值改为200，成功: " + b
					+ ", 当前版本号为: " + stampedReference.getStamp());

			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// 1秒后，再改回原来的100
			System.out.println(Thread.currentThread().getName() + "修改前版本号：" + stampedReference.getStamp());
			System.out.println(Thread.currentThread().getName() + "修改前值：" + stampedReference.getReference());
			b = stampedReference.compareAndSet(stampedReference.getReference(), 100, stampedReference.getStamp(), stampedReference.getStamp() + 1);
			System.out.println(Thread.currentThread().getName() + "将值改回原来的100，成功: " + b
					+ ", 当前版本号为: " + stampedReference.getStamp());
		}, "resolve aba thread1").start();

		// 1秒后，线程2想要从100改为300，值虽然相同，但是版本号不同，修改失败
		new Thread(() -> {
			// 版本号故意设置为不是最新的，造成版本号不一致
			int stamp = stampedReference.getStamp();
			Integer reference = stampedReference.getReference();
			System.out.println(reference == 100);
			System.out.println(Thread.currentThread().getName() + "修改前版本号：" + stamp);
			System.out.println(Thread.currentThread().getName() + "修改前值：" + reference);
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 3秒后，来修改，虽然值相同，但是版本号不同，修改失败
			boolean b = stampedReference.compareAndSet(reference, 300, stamp, stamp + 1);
			System.out.println(Thread.currentThread().getName() + "想要改为300，成功：" + b
					+ ", 版本号为: " + stampedReference.getStamp());
		}, "resolve aba thread2").start();

		// 以下这个demo在jdk8不能成功执行，只有第一次更新能够修改成功，其他都是失败
		// 原因：期望值与原始值比较使用的是"=="，即Integer与int比较，值超过127时，== 比较返回false
		// 因为-128到127之间的Integer会进行缓存，存在常量池中，直接取值，==比较返回true，超过127则需要new Integer()，==比较返回false。
		// 所以需要使用stampedReference.getReference()来获取当前值再进行修改

		// AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(100, 0);
		// new Thread(() -> {
		// 	// 先改为200
		// 	/* 四个参数：
		// 	 * expectedReference: 期望的值，用来比较
		// 	 * newReference: 更新的新值
		// 	 * expectedStamp: 期望的原始标记（版本号）
		// 	 * newStamp: 更新的新版本号
		// 	 */
		// 	System.out.println("修改前版本号：" + stampedReference.getStamp());
		// 	System.out.println("修改前值：" + stampedReference.getReference());
		// 	boolean b = stampedReference.compareAndSet(100, 128, stampedReference.getStamp(), stampedReference.getStamp() + 1);
		// 	System.out.println(Thread.currentThread().getName() + "将值改为200，成功: " + b
		// 			+ ", 当前版本号为: " + stampedReference.getStamp());
		//
		// 	// 再改回原来的100
		// 	System.out.println("修改前版本号：" + stampedReference.getStamp());
		// 	System.out.println("修改前值：" + stampedReference.getReference());
		// 	b = stampedReference.compareAndSet(128, 100, stampedReference.getStamp(), stampedReference.getStamp() + 1);
		// 	System.out.println(Thread.currentThread().getName() + "将值改回原来的100，成功: " + b
		// 			+ ", 当前版本号为: " + stampedReference.getStamp());
		// }, "resolve aba thread1").start();
		//
		// try {
		// 	TimeUnit.SECONDS.sleep(2);
		// } catch (InterruptedException e) {
		// 	e.printStackTrace();
		// }
		//
		// // 1秒后，线程2想要从100改为300，值虽然相同，但是版本号不同，修改失败
		// new Thread(() -> {
		// 	System.out.println("修改前版本号：" + stampedReference.getStamp());
		// 	System.out.println("修改前值：" + stampedReference.getReference());
		// 	boolean b = stampedReference.compareAndSet(100, 300, stampedReference.getStamp(), stampedReference.getStamp() + 1);
		// 	System.out.println(Thread.currentThread().getName() + "想要改为300，成功：" + b
		// 			+ ", 版本号为: " + stampedReference.getStamp());
		// }, "resolve aba thread2").start();
	}
}