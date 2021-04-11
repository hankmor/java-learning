package com.belonk.concurrent.cas;

/**
 * 指令重排：处理器为了提高程序运行效率，可能会对输入代码进行优化，它不保证程序中各个语句的执行先后顺序同代码中的顺序一致，
 * 但是它会保证程序最终执行结果和代码顺序执行的结果是一致的。
 * <p>
 * 编译器进行指令重排的步骤：
 * <pre>
 * 源代码 -> 编译器优化的重排 -> 指令并行的重排 -> 内存系统的重排 -> 最终执行的指令
 * </pre>
 * 处理器在进行指令重排时需要考虑数据的依赖性, 所以能够保证最终执行结果与代码顺序执行一致。
 * </p>
 * <p>
 * <p>多线程中，由于指令重排的存在，多个线程操作的共享变量的一致性是无法保证的，结果无法预测。
 * <p>
 * Java中，可以通过volatile关键字禁止对其修饰的变量进行指令重排，其含义如下：
 * <ul>
 * <li>当程序执行到volatile变量的读操作或者写操作时，在其前面的操作的更改肯定全部已经进行，且结果已经对后面的操作可见；在其后面的操作肯定还没有进行
 * <li>在进行指令优化时，不能将在对volatile变量的读操作或者写操作的语句放在其后面执行，也不能把volatile变量后面的语句放到其前面执行
 * </ul>
 * </p>
 * <p>
 * 另外，遵循happens-before(先行发生)原则的代码也能保证其有序性，否则，编译器可以对其
 * 进行重排序。happens-before原则如下：https://www.cnblogs.com/chenssy/p/6393321.html
 * <ol>
 * <li>程序次序规则：<b>一个线程(单线程)内，按照代码顺序，书写在前面的操作先行发生于书写在后面的操作。</b>即是说，一段代码在单线程中执行的结果是有序的。
 * 注意是执行结果，因为虚拟机、处理器会对指令进行重排序。虽然重排序了，但是并不会影响程序的执行结果，所以程序最终执行的结果与顺序执行的结果是一致的。
 * 故而这个规则只对单线程有效，在多线程环境下无法保证正确性。
 * <li>锁定规则：<b>一个unLock操作先行发生于后面对同一个锁额lock操作。</b>无论是在单线程环境还是多线程环境，一个锁处于被锁定状态，
 * 那么必须先执行unlock操作后面才能进行lock操作。
 * <li>volatile变量规则：<b>对一个变量的写操作先行发生于后面对这个变量的读操作。</b>这个规则说明volatile保证了线程可见性。
 * 如果一个线程先去写一个volatile变量，然后一个线程去读这个变量，那么这个写操作一定是先行发生于读操作的。
 * <li>传递规则：<b>如果操作A先行发生于操作B，而操作B又先行发生于操作C，则可以得出操作A先行发生于操作C。</b>即是说，happens-before原则具有传递性。
 * <li>线程启动规则：<b>Thread对象的start()方法先行发生于此线程的每一个动作。</b>假定线程A在执行过程中，通过执行B.start()来启动线程B，
 * 那么线程A对共享变量的修改在接下来线程B开始执行后确保对线程B可见。
 * <li>线程中断规则：<b>对线程interrupt()方法的调用先行发生于被中断线程的代码检测到中断事件的发生。</b>
 * <li>线程终结规则：<b>线程中所有的操作都先行发生于线程的终止检测，我们可以通过Thread.join()方法结束、Thread.isAlive()的返回值手段
 * 检测到线程已经终止执行。</b>假定线程A在执行的过程中，通过使用B.join()等待线程B终止，那么线程B在终止之前对共享变量的修改在线程A等待返回后可见。
 * <li>对象终结规则：<b>一个对象的初始化完成先行发生于他的finalize()方法的开始。</b>
 * </ol>
 * <p>
 * <p>
 * 指令重排底层是A通过内存屏障实现的，内存屏障是通过CPU指令控制重排序和内存可见性。
 * LoadLoad屏障：对于这样的语句Load1; LoadLoad; Load2，在Load2及后续读取操作要读取的数据被访问前，保证Load1要读取的数据被读取完毕。
 * StoreStore屏障：对于这样的语句Store1; StoreStore; Store2，在Store2及后续写入操作执行前，保证Store1的写入操作对其它处理器可见。
 * LoadStore屏障：对于这样的语句Load1; LoadStore; Store2，在Store2及后续写入操作被刷出前，保证Load1要读取的数据被读取完毕。
 * StoreLoad屏障：对于这样的语句Store1; StoreLoad; Load2，在Load2及后续所有读取操作执行前，保证Store1的写入对所有处理器可见。它的开销是四种屏障中最大的。
 * <p>
 * Created by sun on 2020/7/27.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class InstructionReorderingDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		test1();
		test2();
		test3();
	}

	public static void test1() {
		A data = new A();
		data.f();
	}

	public static void test2() {
		// 执行一万次
		// 注意：测试时，由于指令重排影响程序结果正确性的概率非常小，不易于测试，但是问题是客观存在的
		final boolean[] show20 = {false};
		final boolean[] show10 = {false};
		for (int i = 0; i < 1000; i++) {
			B r = new B();
			new Thread(() -> {
				r.f1();
			}, "t1").start();

			new Thread(() -> {
				int ret = r.f2();
				if (ret == 20) {
					if (!show20[0]) {
						System.out.println("a的值为20");
					}
					show20[0] = true;
				} else if (ret == 10) {
					if (!show10[0]) {
						System.out.println("a的值为10");
					}
					show10[0] = true;
				}
			}, "t2").start();
		}
	}

	public static void test3() {
		C c = new C();
		c.f();
	}
}

class A {
	public void f() {
		int a = 10; // <1>
		int b = 20; // <2>
		a = a + 10;  // <3>
		b += a;  // <4>
		System.out.println(b);
		/*
		 * 由于指令重排的存在，所以执行顺序不一定是1234，可能的顺序是：1234,2134,1324
		 * 但是不可能4在3前边执行，因为4中需要依赖a的值，而a的值需要在3中计算得出
		 * 另外，4也不能在2之前执行，因为变量b需要先申明，再使用
		 */
	}
}

class B {
	// volatile关键字可以禁止指令重排
	private /*volatile*/ int a = 0;
	private /*volatile*/ boolean flag = false;

	public void f1() {
		// 多个线程调用该方法，如果指令重排，可能出现flag = true先于 a = 1执行
		a = 10; // <1>
		flag = true; // <2>
	}

	public int f2() {
		// 多线程环境，flag可能被改为了true，此时，f1()中的 a = 1可能还没执行
		if (flag) {
			// 多线程环境，执行到这一句，a可能是0，也可能是10
			a += 10;
			// 所以，a的值可能是10，也可能是20
		}
		return a;
	}
}

class C {
	private int a = 8;
	// volatile修饰
	private volatile boolean flag = false;

	/**
	 * 注意这里的flag是volatile修饰的，表示禁止指令重排。其含义为：
	 * 第3行代码不会再1、2行代码之前执行，一定是在1、2都执行完之后执行；也不会在4、5行之后执行，一定是在4、5行之后之前执行。但是，1、2行
	 * 的执行顺序并不保证，可能2先于1执行。
	 */
	public void f() {
		int a = 10; // <1>
		int b = 20; // <2>
		flag = true; // <3>
		a = a + 10;  // <4>
		b += a;  // <5>
		System.out.println(b);
	}
}