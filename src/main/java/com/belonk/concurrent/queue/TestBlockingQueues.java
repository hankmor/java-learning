package com.belonk.concurrent.queue;

import com.belonk.concurrent.thread.LiftOff;

import java.util.concurrent.*;

/**
 * Created by sun on 2021/12/29.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class TestBlockingQueues {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 测试各个 BlockingQueue 组件。
	 *
	 * BlockingQueue组件的方法分类:
	 * ------------------------------------------------------------
	 * 方法     抛出异常   返回特殊值  阻塞     等待直到超时
	 * ------------------------------------------------------------
	 * Insert   add(e)    offer(e)  put(e)   offer(e, time, unit)
	 * Remove   remove()  poll()    take()   poll(time, unit)
	 * Examine  element() peek()    不可用    不可用
	 * ------------------------------------------------------------
	 *
	 */

	public static void main(String[] args) throws Exception {
		// ArrayBlockingQueue：有界、基于数组实现，必须指定容量
		test("ArrayBlockingQueue ", new ArrayBlockingQueue<>(3));

		// LinkedBlockingQueue：无界，链表结构，可以指定容量
		test("LinkedBlockingQueue ", new LinkedBlockingQueue<>());

		// SynchronousQueue: 不存储任何元素，所有的添加操作需要等待其他线程的移除操作，反之亦然
		test("SynchronousQueue ", new SynchronousQueue<>());

		// LinkedBlockingDeque：双向，无界，链表结构，可以指定容量
		test("LinkedBlockingDeque ", new LinkedBlockingDeque<>());

		/*
		 * PriorityBlockingQueue：支持优先级排序，无界，可以指定容量，不允许插入不可比较的元素
		 * 由于 LiftOff 未实现 Comparable 接口，不可比较，会抛出 ClassCastException 异常
		 * 但是 PriorityLiftOff 则可以比较，可以看到按照id从大到小出队
		 */
		test("PriorityBlockingQueue ", new PriorityBlockingQueue<>(), PriorityLiftOff.class);

		/*
		 * DelayQueue: 无界，元素必须实现 Delayed 接口，该接口可以设定延迟时间，时间到期时才可以从队列获取，没有元素过期，则没有头节点，poll 返回 null
		 * DelayLiftOff 实现了 Delayed 接口
		 */
		test("DelayQueue ", new DelayQueue<>(), DelayLiftOff.class);

		// LinkedTransferQueue: 实现了 TransferQueue 接口，如果有消费者线程在等待介绍元素，则队列可以将生产者加入的元素立即传输给消费者，没有则将元素放入队列尾部
		test("LinkedTransferQueue  ", new LinkedTransferQueue<>());
	}

	public static void test(String msg, BlockingQueue<LiftOff> rockets) throws Exception {
		test(msg, rockets, LiftOff.class);
	}

	public static <T extends LiftOff> void test(String msg, BlockingQueue<T> rockets, Class<T> clazz) throws Exception {
		System.out.println(msg);
		// 先启动发射器，此时在等待装载火箭
		Thread thread = new Thread(new LiftOffRunner(rockets));
		thread.start();
		// 向队列中加入LiftOff，即：装载火箭
		try {
			for (int i = 0; i < 5; i++) {
				// 如果put 换成 add 方法，则队列满时抛出 IllegalStateException: Queue full
				rockets.put(clazz.getConstructor(int.class).newInstance(5));
			}
		} catch (InterruptedException | NoSuchMethodException e) {
			System.out.println("火箭准备工作被中断");
		}
		// 等待一段时间后终止线程
		TimeUnit.SECONDS.sleep(1);
		thread.interrupt();
	}
}

class LiftOffRunner implements Runnable {
	public BlockingQueue<? extends LiftOff> rockets;

	public LiftOffRunner(BlockingQueue<? extends LiftOff> rockets) {
		this.rockets = rockets;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				// take方法：获取不到，则阻塞等待
				LiftOff liftOff = rockets.take();
				liftOff.run();
				System.out.println();
			}
		} catch (InterruptedException e) {
			System.out.println("发射成功！");
		}
	}
}
