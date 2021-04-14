package com.belonk.concurrent.thread.deprecate;

import com.belonk.concurrent.util.SleeperUtil;
import com.belonk.jdk8.date.DateUtil;
import com.belonk.util.PrintHelper;

import java.util.Date;

/**
 * Created by sun on 2021/4/13.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class DeprecatedApi {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		// apiUsing();
		// deadlockBySuspend1();
		deadlockBySuspend2();
		// insecureStop();
	}

	public static void apiUsing() {
		Thread thread = new Thread(new DeprecatedClass(), "thread-using-deprecated-api");
		thread.start();

		// 运行一段时间后，暂停线程
		SleeperUtil.seconds(2);
		thread.suspend();
		PrintHelper.println(thread.getName() + " is suspended : " + DateUtil.format(new Date(), "HH:mm:ss"));

		// 运行一段时间后，恢复线程
		SleeperUtil.seconds(2);
		thread.resume();
		PrintHelper.println(thread.getName() + " is resumed : " + DateUtil.format(new Date(), "HH:mm:ss"));

		// 运行一段时间后，终止线程
		SleeperUtil.seconds(2);
		thread.stop();
		PrintHelper.println(thread.getName() + " is stopped : " + DateUtil.format(new Date(), "HH:mm:ss"));

		// 不立即退出
		SleeperUtil.seconds(2);
	}

	public static void deadlockBySuspend1() {
		/*
		suspend造成死锁可能的原因：
		1、调用suspend未调用resume，导致suspend的线程一直占用锁
		2、先调用resume，再调用suspend，导致suspend的线程一直占用锁

		现象：main thread end不能输出，然后线程卡住
		死锁原因：System.out.println中加锁，该锁为PrintStream实例，假设为lock：
		public void println(String x) {
            synchronized (this) {
	            print(x);
	            newLine();
	        }
	    }
		thread线程运行一段时间后暂停，如果刚好暂停在println的同步代码块中(其中的pring(x)中有重入锁)，那么它不会释放lock锁。
		然后main线程得不到锁，也在等待lock锁，造成死锁。
		 */
		Thread thread = new Thread(() -> {
			int i = 0;
			while (i < 100) {
				i++;
				System.out.println(i);
				SleeperUtil.milliseconds(10);
			}
		}, "suspend-deadlock");
		thread.start();
		SleeperUtil.seconds(1);
		thread.suspend();

		// 如果不调用resume，则会死锁
		// SleeperUtil.seconds(1);
		// thread.resume();

		// 可能不能打印这一句，造成main线程和thread线程都卡住(死锁)
		System.out.println("main thread end");

		/*
		jstack输出的信息可以看到，main线程BLOCKED在PrintStream上，suspend-deadlock暂停在 FileOutputStream.writeBytes(Native Method)

		"suspend-deadlock" #11 prio=5 os_prio=31 tid=0x00007feb8e91a000 nid=0x5803 runnable [0x000070000f6dc000]
   java.lang.Thread.State: RUNNABLE
        at java.io.FileOutputStream.writeBytes(Native Method)
        at java.io.FileOutputStream.write(FileOutputStream.java:326)
        at java.io.BufferedOutputStream.flushBuffer(BufferedOutputStream.java:82)
        at java.io.BufferedOutputStream.flush(BufferedOutputStream.java:140)
        - locked <0x000000076eb0fe00> (a java.io.BufferedOutputStream) // 锁定
        at java.io.PrintStream.write(PrintStream.java:482)
        - locked <0x000000076eb088a0> (a java.io.PrintStream)   // 锁定
        at sun.nio.cs.StreamEncoder.writeBytes(StreamEncoder.java:221)
        at sun.nio.cs.StreamEncoder.implFlushBuffer(StreamEncoder.java:291)
        at sun.nio.cs.StreamEncoder.flushBuffer(StreamEncoder.java:104)
        - locked <0x000000076eb0ff20> (a java.io.OutputStreamWriter)
        at java.io.OutputStreamWriter.flushBuffer(OutputStreamWriter.java:185)
        at java.io.PrintStream.newLine(PrintStream.java:546)
        - eliminated <0x000000076eb088a0> (a java.io.PrintStream)
        at java.io.PrintStream.println(PrintStream.java:807)
        - locked <0x000000076eb088a0> (a java.io.PrintStream)
        at com.belonk.concurrent.thread.DeprecatedApi.lambda$deadlockBySuspend1$0(DeprecatedApi.java:63)
        at com.belonk.concurrent.thread.DeprecatedApi$$Lambda$1/396873410.run(Unknown Source)
        at java.lang.Thread.run(Thread.java:748)


		"main" #1 prio=5 os_prio=31 tid=0x00007feb8e80e000 nid=0xe03 waiting for monitor entry [0x000070000e19a000]
		   java.lang.Thread.State: BLOCKED (on object monitor)
		        at java.io.PrintStream.println(PrintStream.java:805)
		        - waiting to lock <0x000000076eb088a0> (a java.io.PrintStream) // 等待锁
		        at com.belonk.concurrent.thread.DeprecatedApi.deadlockBySuspend1(DeprecatedApi.java:69)
		        at com.belonk.concurrent.thread.DeprecatedApi.main(DeprecatedApi.java:30)

		 */
	}

	public static void deadlockBySuspend2() {
		/*
		现象：线程卡住，不能打印任何输出

		死锁原因：
		resume方法先于suspend方法调用，造成线程suspend后不能恢复并锁定
		 */
		DeadLockBySuspend deadLockBySuspend = new DeadLockBySuspend();
		Thread thread = new Thread(deadLockBySuspend, "suspend-deadlock");
		thread.start();
		// 充分运行
		SleeperUtil.seconds(2);
		PrintHelper.println(Thread.currentThread().getName() + " : " + Thread.currentThread().getState()); // RUNNABLE

		// 一个线程暂停
		new Thread(() -> {
			SleeperUtil.seconds(4);
			thread.suspend();
			PrintHelper.println(Thread.currentThread().getName() + " suspends " + thread.getName() + ": " + DateUtil.format(new Date(), "HH:mm:ss.S"));
		}).start();

		// 一个线程恢复
		new Thread(() -> {
			SleeperUtil.seconds(3);
			thread.resume();
			PrintHelper.println(Thread.currentThread().getName() + " resumes " + thread.getName() + ": " + DateUtil.format(new Date(), "HH:mm:ss.S"));
		}).start();

		/*
		输出：

		suspend-deadlock is running : 12:10:33
		suspend-deadlock is running : 12:10:34
		main : RUNNABLE
		suspend-deadlock is running : 12:10:35
		suspend-deadlock is running : 12:10:36
		suspend-deadlock is running : 12:10:37
		Thread-1 resumes suspend-deadlock: 12:10:38.7
		suspend-deadlock is running : 12:10:38
		Thread-0 suspends suspend-deadlock: 12:10:39.7

		jstack看到suspend-deadlock处于TIMED_WAITING状态，其实它已经被挂起
		"suspend-deadlock" #11 prio=5 os_prio=31 tid=0x00007fd7df896000 nid=0xa603 waiting on condition [0x000070000fc39000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
        at java.lang.Thread.sleep(Native Method)
        at java.lang.Thread.sleep(Thread.java:340)
        at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
        at com.belonk.concurrent.util.SleeperUtil.seconds(SleeperUtil.java:25)
        at com.belonk.concurrent.thread.DeadLockBySuspend.run(DeprecatedApi.java:217)
        at java.lang.Thread.run(Thread.java:748)
		 */
	}

	public static void insecureStop() {
		InsecureStop insecureStop = new InsecureStop();
		Thread thread = new Thread(insecureStop, "insecure-stop");
		thread.start();
		SleeperUtil.seconds(2);
		// 调用stop过后，线程立即终止，造成数据与期望的不一致
		thread.stop();
		PrintHelper.println(thread.getState());
		PrintHelper.println("y = " + insecureStop.x + ", expect x = 10");
		PrintHelper.println("y = " + insecureStop.y + ", expect y = 11");
		/*
		y = 10, expect x = 10
		y = 2, expect y = 11
		 */
	}
}

class DeprecatedClass implements Runnable {
	@Override
	public void run() {
		while (true) {
			PrintHelper.println(Thread.currentThread().getName() + " is running : " + DateUtil.format(new Date(), "HH:mm:ss"));
			SleeperUtil.seconds(1);
		}
	}
}

class DeadLockBySuspend implements Runnable {
	@Override
	public void run() {
		while (true) {
			PrintHelper.println(Thread.currentThread().getName() + " is running : " + DateUtil.format(new Date(), "HH:mm:ss"));
			SleeperUtil.seconds(1);
		}
	}
}

class InsecureStop implements Runnable {
	int x = 1;
	int y = 2;

	@Override
	public void run() {
		x = 10;
		SleeperUtil.seconds(5);
		y = 11;
	}
}