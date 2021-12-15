package com.belonk.concurrent.queue;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by sun on 2021/4/28.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class BlockqingQueuePerformance {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		int c = 1000 * 10000;
		int md = c / 2;
		Set<Integer> set = new HashSet<>(c);
		// BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(c);
		BlockingQueue<Integer> queue = new LinkedBlockingDeque<>(c);
		// BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(c);
		for (int i = 0; i < c; i++) {
			queue.add(i);
			set.add(i);
		}
		for (int i = 0; i < 10; i++) {
			long s = System.currentTimeMillis();
			boolean contains = queue.contains(md);
			long e = System.currentTimeMillis();
			System.out.println("第" + i + "次：" + (e - s) + "ms");
		}
		System.out.println("============= set =============");
		for (int i = 0; i < 10; i++) {
			long s = System.currentTimeMillis();
			boolean contains = set.contains(md);
			long e = System.currentTimeMillis();
			System.out.println("第" + i + "次：" + (e - s) + "ms");
		}

		/*
		1000万：
		1、ArrayBlockingQueue：
		第0次：16ms
		第1次：13ms
		第2次：15ms
		第3次：12ms
		第4次：13ms
		第5次：12ms
		第6次：12ms
		第7次：12ms
		第8次：13ms
		第9次：13ms
		============= set =============
		第0次：0ms
		第1次：0ms
		第2次：0ms
		第3次：0ms
		第4次：0ms
		第5次：0ms
		第6次：0ms
		第7次：0ms
		第8次：0ms
		第9次：0ms

		2、LinkedBlockingQueue
		第0次：32ms
		第1次：26ms
		第2次：27ms
		第3次：26ms
		第4次：26ms
		第5次：25ms
		第6次：25ms
		第7次：95ms
		第8次：36ms
		第9次：36ms
		============= set =============
		第0次：0ms
		第1次：0ms
		第2次：0ms
		第3次：0ms
		第4次：0ms
		第5次：0ms
		第6次：0ms
		第7次：0ms
		第8次：0ms
		第9次：0ms

		3、LinkedBlockingDeque
		第0次：48ms
		第1次：42ms
		第2次：45ms
		第3次：47ms
		第4次：47ms
		第5次：50ms
		第6次：49ms
		第7次：48ms
		第8次：51ms
		第9次：46ms
		============= set =============
		第0次：0ms
		第1次：0ms
		第2次：0ms
		第3次：0ms
		第4次：0ms
		第5次：0ms
		第6次：0ms
		第7次：0ms
		第8次：0ms
		第9次：0ms
		 */
	}
}