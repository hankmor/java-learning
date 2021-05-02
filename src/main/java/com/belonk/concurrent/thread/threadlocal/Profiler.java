package com.belonk.concurrent.thread.threadlocal;

import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/5/2.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class Profiler {
	//~ Static fields/constants/initializer

	// 初始化值的方法，第一次get时会调用
	public static final ThreadLocal<Long> THREAD_LOCAL = ThreadLocal.withInitial(System::currentTimeMillis);

	//~ Instance fields


	//~ Constructors


	//~ Methods

	public void begin() {
		THREAD_LOCAL.set(System.currentTimeMillis());
	}

	public long end() {
		return System.currentTimeMillis() - THREAD_LOCAL.get();
	}

	public static void main(String[] args) throws InterruptedException {
		/*
		ThreadLocal: 内部为ThreadLocalMap对象，key为ThreadLocal对象，value为任意对象
		 */
		Profiler profiler = new Profiler();
		profiler.begin();
		TimeUnit.SECONDS.sleep(2);
		System.out.println("Spend time : " + profiler.end());
	}
}