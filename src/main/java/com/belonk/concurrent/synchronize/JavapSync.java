package com.belonk.concurrent.synchronize;

/**
 * Created by sun on 2021/4/14.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class JavapSync {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		/*
		定位到JavapSync.class所在目录，然后使用javap -v JavapSync查看字节码详细信息

		指令ACC_SYNCHRONIZED：保证方法同步
		monitorenter、monitorexit同步代码块
		 */
	}

	// 同步方法
	public synchronized void syncMethod1() {
		System.out.println("syncMethod1");
	}

	// 同步代码块，SyncClass.class作为锁
	public void syncMethod2() {
		synchronized (JavapSync.class) {
			System.out.println("syncMethod2");
		}
	}

	// 同步代码块，对象实例作为锁
	public void syncMethod3() {
		synchronized (this) {
			System.out.println("syncMethod3");
		}
	}

	// 静态同步方法，SyncClass.class作为锁
	public static synchronized void syncMethod4() {
		System.out.println("syncMethod4");
	}

	// 未同步方法
	public void method() {
		System.out.println("normal method");
	}
}