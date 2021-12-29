package com.belonk.concurrent.queue;

import com.belonk.concurrent.thread.LiftOff;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/12/29.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class DelayLiftOff extends LiftOff implements Delayed {
	//~ Static fields/constants/initializer

	private static final long now = System.currentTimeMillis();

	//~ Instance fields


	//~ Constructors

	public DelayLiftOff() {
	}

	public DelayLiftOff(int countDown) {
		super(countDown);
	}

	//~ Methods

	/*
	 * 支持延迟时间的 LiftOff
	 *
	 * 按照 id 设置延迟时间，很明显，id 越大则延迟越大
	 */

	@Override
	public long getDelay(TimeUnit unit) {
		/*
		 * 将 id 做为延迟的毫秒数，计算延迟时间
		 *
		 * now                    System.currentTimeMillis()
		 * |——————————————————————|-->
		 * 延迟的毫秒数: this.getId()
		 * |————————————————————————————————————————|
		 *
		 * 系统当前时间差：long period = System.currentTimeMillis() - now;
		 * 剩余延迟时间：long delay = 延迟的毫秒数 - period，如果为负数，则延迟过期，此时 DelayedQueue 就可以从头部取出元素
		 */
		long d = unit.convert(this.getId() - (System.currentTimeMillis() - now), TimeUnit.MILLISECONDS);
		// System.out.println("delay: " + d);
		return d;
	}

	@Override
	public int compareTo(Delayed o) {
		long d1 = this.getDelay(TimeUnit.MILLISECONDS);
		long d2 = o.getDelay(TimeUnit.MILLISECONDS);
		// 顺序
		// return Long.compare(d1, d2);
		// 倒序
		return Long.compare(d2, d1);
	}
}