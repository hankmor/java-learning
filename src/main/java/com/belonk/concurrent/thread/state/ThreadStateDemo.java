package com.belonk.concurrent.thread.state;

import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/4/11.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ThreadStateDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		/*
		 * 线程的六个状态：
		 * 1、NEW：线程刚创建，还没调用start启动
		 * 2、RUNNABLE: 线程调用了start，但是是否真正开始执行需要依赖操作系统调度，Java中把初始化(start了但是还没有正在开始执行)和正在运行合并为一个RUNNABLE状态
		 * 3、WAITING: 线程处于等待状态，需要其他线程来唤醒或者中断该线程
		 * 4、TIMED_WAITING: 限时等待，线程等待一定的时间，然后时间到了会自动唤醒
		 * 5、BLOCKED: 阻塞状态，线程在等待获得锁
		 * 5、TERMINATED: 线程执行完成
		 */
		// 查看线程状态：jps, jstack [pid]
		// 新创建线程，但是没有调用start启动，无法通过jstack监视到
		Thread newThread = new Thread(new New(), "new-thread");
		System.out.println(newThread.getState()); // 通过打印线程的状态，可以看到处于NEW状态
		Thread thread = new Thread(new ThreadRunnable(), "runnable-thread");
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
		// Object.wait()后，现场处于等待状态
		new Thread(new Waiting(), "waiting-thread").start();
		// 限时等待60秒
		new Thread(new TimedWaiting(), "timedwaiting-thread").start();
		// 两个阻塞线程，互相竞争同一把锁，没有竞争到的处于BLOCKED状态
		new Thread(new Blocked(), "blocked-tread1").start();
		new Thread(new Blocked(), "blocked-tread2").start();
		// TERMINATED难以捕获，执行完成就被销毁了
		new Thread(new Terminated(), "terminated-tread2").start();

		/*
		结果：
		1、jsp
		2096 Jps
		9824 ThreadStateDemo
		2756 Launcher
		436
		9660 RemoteMavenServer36
		2、jstack 9824
"DestroyJavaVM" #18 prio=5 os_prio=0 tid=0x00000000032b2800 nid=0x134 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

"blocked-tread2" #16 prio=5 os_prio=0 tid=0x0000000019565000 nid=0x3f04 waiting on condition [0x000000001a32e000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
        at java.lang.Thread.sleep(Native Method)
        at java.lang.Thread.sleep(Thread.java:340)
        at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
        at com.belonk.concurrent.ts.Blocked.run(ThreadStateDemo.java:222)
        - locked <0x00000000d60f20d0> (a java.lang.Class for com.belonk.concurrent.ts.Blocked)
        at java.lang.Thread.run(Thread.java:748)

"blocked-tread1" #15 prio=5 os_prio=0 tid=0x0000000019564800 nid=0xbe8 waiting for monitor entry [0x000000001a22f000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at com.belonk.concurrent.ts.Blocked.run(ThreadStateDemo.java:222)
        - waiting to lock <0x00000000d60f20d0> (a java.lang.Class for com.belonk.concurrent.ts.Blocked)
        at java.lang.Thread.run(Thread.java:748)

"timedwaiting-thread" #14 prio=5 os_prio=0 tid=0x000000001955c000 nid=0x1854 waiting on condition [0x000000001a12f000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
        at java.lang.Thread.sleep(Native Method)
        at java.lang.Thread.sleep(Thread.java:340)
        at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
        at com.belonk.concurrent.ts.TimedWaiting.run(ThreadStateDemo.java:208)
        at java.lang.Thread.run(Thread.java:748)

"waiting-thread" #13 prio=5 os_prio=0 tid=0x000000001955a800 nid=0x1d0 in Object.wait() [0x000000001a02f000]
   java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000d60eccd0> (a java.lang.Class for com.belonk.concurrent.ts.Waiting)
        at java.lang.Object.wait(Object.java:502)
        at com.belonk.concurrent.ts.Waiting.run(ThreadStateDemo.java:194)
        - locked <0x00000000d60eccd0> (a java.lang.Class for com.belonk.concurrent.ts.Waiting)
        at java.lang.Thread.run(Thread.java:748)

"runnable-thread" #12 prio=1 os_prio=-2 tid=0x000000001951c800 nid=0xfc8 runnable [0x0000000019f2f000]
   java.lang.Thread.State: RUNNABLE
        at com.belonk.concurrent.ts.New.run(ThreadStateDemo.java:172)
        at java.lang.Thread.run(Thread.java:748)

		 */
	}
}

class New implements Runnable {
	@Override
	public void run() {
		while (true) {
		}
	}
}

class ThreadRunnable implements Runnable {
	@Override
	public void run() {
		while (true) {
			// 使得当前线程一直y=运行，这样就可以看到RUNNABLE状态
		}
	}
}

class Waiting implements Runnable {
	@Override
	public void run() {
		synchronized (Waiting.class) {
			while (true) {
				try {
					// 需要加锁才能调用wait
					Waiting.class.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class TimedWaiting implements Runnable {
	@Override
	public void run() {
		while (true) {
			try {
				TimeUnit.SECONDS.sleep(60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class Blocked implements Runnable {
	@Override
	public void run() {
		while (true) {
			synchronized (Blocked.class) {
				try {
					TimeUnit.SECONDS.sleep(60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

class Terminated implements Runnable {
	@Override
	public void run() {
	}
}