package com.belonk.concurrent.queue;

import com.belonk.concurrent.thread.LiftOff;

/**
 * Created by sun on 2021/12/29.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class PriorityLiftOff extends LiftOff implements Comparable<PriorityLiftOff> {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors

	/*
	 * 支持优先级的 LiftOff 对象
	 *
	 * 按照 id 从大到小设置优先级，越大的优先级越高
	 */

	public PriorityLiftOff() {
	}

	public PriorityLiftOff(int countDown) {
		super(countDown);
	}

	//~ Methods


	@Override
	public int compareTo(PriorityLiftOff o) {
		// 按照id从大到小排列
		return this.getId() > o.getId() ? -1 : 1;
	}
}