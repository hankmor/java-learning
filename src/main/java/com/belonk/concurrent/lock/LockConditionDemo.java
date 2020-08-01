package com.belonk.concurrent.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by sun on 2020/7/23.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class LockConditionDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/**
	 * 题目：三个线程A、B、C，按顺序分别打印5、10、15次，然后重复10次。
	 * 不能使用sleep。
	 */
	public static void main(String[] args) {
		Resource resource = new Resource();
		resource.addThread(new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				resource.print(5);
			}
		}), new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				resource.print(10);
			}
		}), new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				resource.print(15);
			}
		})).start();
	}
}

class Resource {
	private final Lock lock = new ReentrantLock();

	private long tag;
	private Node first;
	private Node last;

	// 线程和条件持有类，每个线程一个condition，用于按顺序唤醒
	private static class ThreadHolder {
		Thread thread;
		Condition condition;

		ThreadHolder(Thread thread, Condition condition) {
			this.thread = thread;
			this.condition = condition;
		}
	}

	private static class Node {
		ThreadHolder item;
		Node next;
		Node prev;

		Node(Node prev, ThreadHolder element, Node next) {
			this.item = element;
			this.next = next;
			this.prev = prev;
		}
	}

	private Node find(Thread thread) {
		if (first == null) {
			throw new NullPointerException("Do not found any threads.");
		}
		return find(first, thread.getId());
	}

	private Node find(Node node, long id) {
		if (node.item.thread.getId() == id) {
			return node;
		}
		return find(node.next, id);
	}

	public Resource addThread(Thread... thread) {
		int idx = 0;
		for (Thread t : thread) {
			Node l = last;
			Node node = new Node(l, new ThreadHolder(t, lock.newCondition()), null);
			last = node;
			if (l == null) {
				first = node;
			} else {
				l.next = node;
			}
		}
		// 最后一个节点的下一个变为第一个节点，形成闭环
		if (last.next == null) {
			last.next = first;
		}
		if (first.prev == null) {
			first.prev = last;
		}
		return this;
	}

	public Resource start() {
		start(first);
		return this;
	}

	private void start(Node node) {
		node.item.thread.start();
		if (node.next != first) {
			start(node.next);
		}
	}

	public void print(int number) {
		lock.lock();
		try {
			Thread thread = Thread.currentThread();
			Node node = find(thread);

			if (tag == 0) {
				tag = node.item.thread.getId();
			}

			// 判断条件，按照线程添加的先后顺序作为判断标志
			while (tag != node.item.thread.getId()) {
				node.item.condition.await();
			}

			for (int i = 0; i < number; i++) {
				System.out.println(Thread.currentThread().getName() + "\t" + (i + 1));
			}
			// 打印完成，tag变更为下一个线程的id
			tag = node.next.item.thread.getId();
			// 通知下一个线程开始打印
			node.next.item.condition.signal();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}