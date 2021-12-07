package com.belonk.collection.ref;

import java.lang.ref.*;
import java.util.LinkedList;

/**
 * Created by sun on 2021/12/7.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ReferenceDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * Reference表示对象的引用关系，它是一个抽象类，它和其派生子类被设计用来为垃圾回收器提供不同级别的间歇性指示。
	 *
	 * Java有四种引用类型：
	 *
	 * 1、强引用：没有被 Reference 包装的对象存在引用关系，不能被垃圾回收，这种引用称为强引用
	 * 2、软引用：{@link java.lang.ref.SoftReference}，由垃圾收集器根据内存需求自行清除。软引用最常用于实现内存敏感缓存。
	 * 3、弱引用：{@link java.lang.ref.WeakReference}，弱引用对象，不会阻止它们的引用对象被终结、回收。弱引用最常用于实现规范化映射。
	 * 4、幻象引用：{@link java.lang.ref.PhantomReference}，又称为虚引用。在收集器确定它们的引用对象可能会被回收后排队。 幻像引用最常用于以比
	 * Java 终结机制更灵活的方式调度预检清理操作。
	 *
	 * 后三种引用类都继承自抽象类 {@link java.lang.ref.Reference}。
	 *
	 * ReferenceQueue：引用队列，它是一个用作回收前清理的工具。在检测到适当的可达性更改后，垃圾收集器会将已注册的引用对象附加到这些队
	 * 列中。
	 *
	 * 引用实例处于四种可能的内部状态之一：
	 *
	 * 1、Active（活动）：受到垃圾收集器的特殊处理。在收集器检测到所指对象的可达性已更改为适当状态后一段时间，它会将实例的状态更改为
	 * Pending 或 Inactive，具体取决于实例在创建时是否已向队列注册。在前一种情况下，它还会将实例添加到待定引用列表中。新创建的实例处于
	 * 活动状态。
	 *
	 * 2、Pending（挂起）：挂起引用列表的一个元素，等待引用处理程序线程入队。未注册的实例永远不会处于这种状态。
	 *
	 * 3、Enqueued（入队）：实例在创建时注册到ReferenceQueue队列。当一个实例从它的 ReferenceQueue 中移除时，它会被设置为 Inactive。
	 * 未注册的实例永远不会处于这种状态。
	 *
	 * 4、Inactive（不活动）：无事可做。一旦实例变为非活动状态，其状态将永远不会再改变。
	 *
	 * 状态在队列和下一个字段中编码如下：
	 *
	 * 1、活动：队列 = 注册实例的 ReferenceQueue，或者 ReferenceQueue.NULL 如果它没有注册到队列； next = null。
	 *
	 * 2、待定：队列 = 注册实例的 ReferenceQueue； next = this
	 *
	 * 3、Enqueued: queue = ReferenceQueue.ENQUEUED; next = 队列中的后续实例，如果在列表末尾则 next = this。
	 *
	 * 4、不活动：队列 = ReferenceQueue.NULL; next = this。
	 *
	 * 使用这种方案，收集器只需要检查下一个字段以确定引用实例是否需要特殊处理：如果下一个字段为空，则该实例处于活动状态； 如果它不为空，
	 * 则收集器应正常处理该实例。 为确保并发收集器可以发现活动的引用对象，而不会干扰可能将 enqueue() 方法应用于这些对象的应用程序线程，
	 * 收集器应通过已发现的字段链接已发现的对象。发现的字段还用于链接挂起列表中的参考对象。
	 */

	public static void main(String[] args) {
		References refs = new References();
		refs.test();
	}
}

class VeryBig {
	private static final int size = 1000;
	private long[] data = new long[size];
	private String id;

	public VeryBig(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "VeryBig[id = " + id + "]";
	}

	// 被垃圾回收时会执行finalize方法
	protected void finalize() {
		System.out.println("VeryBig, id = " + this.id + " is finalizing.");
	}
}

class References {
	private final ReferenceQueue<VeryBig> refQueue = new ReferenceQueue<>();

	private int size = 10;

	public References() {
	}

	public References(int size) {
		this.size = size;
	}

	/**
	 * 检查队列中对象是否存在
	 * <p>
	 * 如果poll()的结果为null，说明对象已经被垃圾回收，尽管还被reference.get()引用
	 */
	public void checkQueue() {
		// 从队列中删除一个并返回，如果队列为空，则返回null，说明被垃圾回收了
		Reference<? extends VeryBig> reference = refQueue.poll();
		if (reference != null) {
			// 获取被删除的对象，reference.get() 方法调用会引用对象
			System.out.println("VeryBig : " + reference.get());
		}
	}

	public void test() {
		int size = this.size;
		// 软引用测试
		LinkedList<SoftReference<VeryBig>> softRefs = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			softRefs.add(new SoftReference<>(new VeryBig("soft-" + i), refQueue));
			// 打印最后创建的对象
			System.out.println("Created : " + softRefs.getLast());
			// 检查引用对象
			checkQueue();
		}

		// 弱引用测试
		LinkedList<WeakReference<VeryBig>> weakRefs = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			weakRefs.add(new WeakReference<>(new VeryBig("weak-" + i), refQueue));
			// 打印最后创建的对象
			System.out.println("Created : " + weakRefs.getLast());
			// 检查引用对象
			checkQueue();
		}

		System.out.println("gc...");
		// 手动触发垃圾回收
		System.gc();

		// 再创建两个引用对象
		SoftReference<VeryBig> s = new SoftReference<>(new VeryBig("soft"));
		WeakReference<VeryBig> w = new WeakReference<>(new VeryBig("weak"));

		// 幻象引用
		LinkedList<PhantomReference<VeryBig>> phantomRefs = new LinkedList<>();
		for (int i = 0; i < size; i++) {
			phantomRefs.add(new PhantomReference<>(new VeryBig("phantom-" + i), refQueue));
			// 打印最后创建的对象
			System.out.println("Created : " + phantomRefs.getLast());
			// 检查引用对象
			checkQueue();
		}
	}
}