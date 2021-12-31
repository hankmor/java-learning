package com.belonk.concurrent.queue;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by sun on 2021/12/30.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ToastStore {
	//~ Static fields/constants/initializer

	//~ Instance fields

	/**
	 * 门店id
	 */
	final int id;
	/**
	 * 吐司的份数id，
	 */
	private int totalGlobalId = 0;
	/**
	 * 最大的吐司数量
	 */
	private final int maxToast;
	/**
	 * 制作机器数量
	 */
	private final int makeMachineNumber;
	/**
	 * 抹黄油的机器数量
	 */
	private final int butterMachineNumber;
	/**
	 * 涂果酱的机器数量
	 */
	private final int jamMachineNumber;
	/**
	 * 监控机器，如果生产了足够数量的吐司，则店铺关门
	 */
	private final CyclicBarrier watchDog;
	private final ExecutorService executorService = Executors.newCachedThreadPool();
	// 考虑单独启动守护线程为顾客服务
	// private final ExecutorService customerServingService = Executors.newCachedThreadPool(new ThreadFactory() {
	// 	@Override
	// 	public Thread newThread(Runnable r) {
	// 		Thread thread = new Thread(r);
	// 		thread.setName("customer-serving-thread");
	// 		thread.setDaemon(true);
	// 		return thread;
	// 	}
	// });
	final BlockingQueue<Toast> driedQueue = new LinkedBlockingQueue<>();
	final BlockingQueue<Toast> butteredQueue = new LinkedBlockingQueue<>();
	final BlockingQueue<Toast> finishedQueue = new LinkedBlockingQueue<>();
	/**
	 * 用于模拟机器运作时间的随机数
	 */
	private final Random random = new Random(47);
	private volatile boolean closed;
	private volatile int totalNumber;


	//~ Constructors

	public ToastStore(int id, int makeMachineNumber, int butterMachineNumber, int jamMachineNumber, int maxToast) {
		this.id = id;
		this.makeMachineNumber = makeMachineNumber;
		this.butterMachineNumber = butterMachineNumber;
		this.jamMachineNumber = jamMachineNumber;
		watchDog = new CyclicBarrier(makeMachineNumber, this::close);
		this.maxToast = maxToast;
		System.out.println(this);
	}


	//~ Methods

	/*
	 * 自动吐司面包制作流程案例：吐司面包需要制作、抹黄油、涂果酱三个步骤，使用阻塞队列实现3个步骤的对象流转。
	 *
	 * 一共需要三个队列来盛放吐司：刚出炉的、已抹黄油的、制作完成的
	 */

	public static void main(String[] args) throws InterruptedException {
		Random random = new Random(47);
		// 多家门店开业了
		for (int i = 0; i < 10; i++) {
			ToastStore toastStore = new ToastStore(i,
					5 + random.nextInt(15),
					2 + random.nextInt(10),
					2 + random.nextInt(5),
					100);
			toastStore.open();

			// 创建顾客
			int customerNumber = 2 + random.nextInt(6);
			for (int j = 0; j < customerNumber; j++) {
				new Customer(j, toastStore);
			}
		}
	}

	/**
	 * 开店营业了
	 */
	public void open() {
		closed = false;
		// 启动制作机器
		for (int i = 0; i < makeMachineNumber; i++) {
			executorService.execute(new Toaster(i));
		}
		// 启动抹黄油的机器
		for (int i = 0; i < butterMachineNumber; i++) {
			executorService.execute(new Butter(i));
		}
		// 启动涂果酱的机器
		for (int i = 0; i < jamMachineNumber; i++) {
			executorService.execute(new Jam(i));
		}
	}

	/**
	 * 来顾客了，提供优质服务
	 */
	public void serving(Customer customer) {
		System.out.println("欢迎您，" + customer);
		// customerServingService.execute(customer);
		if (!this.closed) {
			executorService.execute(customer);
		}
	}

	// 判断是否到达上限，是则关门，多个线程都会判断，需要同步
	public boolean needClose() {
		boolean overflow;
		synchronized (this) {
			overflow = totalNumber >= maxToast;
			if (overflow) {
				closed = true;
			} else {
				++totalNumber;
			}
		}
		Thread.yield();
		if (overflow) {
			if (totalNumber > maxToast) {
				System.err.println(totalNumber);
				throw new RuntimeException("Max toast number over limit: " + this.totalNumber);
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 打烊了
	 */
	public void close() {
		System.out.printf("门店：%-3d 吐司数量已达上限，打烊了: %d%n", this.id, this.totalNumber);
		// 关门了，立即中断机器，如果是 shutdown，可能其他机器仍然在等待获取toast
		this.executorService.shutdownNow();
	}

	public boolean isClosed() {
		return closed;
	}

	@Override
	public String toString() {
		return String.format("门店 %-3d 号：%-3d 台制作机，%-3d 台黄油机，%-3d 台果酱机，最大数量：%d",
				this.id, this.makeMachineNumber, this.butterMachineNumber, this.jamMachineNumber, this.maxToast);
	}

	void print(Object obj, String oper, Toast toast) {
		System.out.printf(obj + " %-4s ：%s \n", oper, toast);
	}

	// 吐司状态：刚制作、抹黄油、涂果酱
	enum ToastStatus {
		DRY, BUTTERED, JAMMED;
	}

	class Toast {
		// 这里的id生成在多线程并发时会重复，因为是非原子操作
		// final int id = ++count;
		final int id;
		ToastStatus status = ToastStatus.DRY;

		public Toast() {
			// 想让每台机器制作的吐司都有唯一的id，即id不重复，需要加锁
			synchronized (ToastStore.this) {
				this.id = ++totalGlobalId;
			}
			if (this.id > ToastStore.this.maxToast) {
				throw new RuntimeException("Max toast id over limit: " + this.id);
			}
		}

		@Override
		public String toString() {
			return String.format("Toast：%-6d(%s)", this.id, this.status);
		}
	}

	class Toaster implements Runnable {
		private final int id;
		private final BlockingQueue<Toast> driedQueue = ToastStore.this.driedQueue;

		public Toaster(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			try {
				while (!ToastStore.this.isClosed()) {
					if (ToastStore.this.needClose()) {
						break;
					}
					Toast toast = new Toast();
					// 模拟制作时间
					TimeUnit.MILLISECONDS.sleep(50 + random.nextInt(200));
					ToastStore.this.print(this, "完成", toast);
					// 放入队列，队列满则阻塞
					driedQueue.put(toast);
				}
				// 等待所有机器都制造完成
				watchDog.await();
			} catch (InterruptedException e) {
				System.out.println(this + "中断");
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
			System.out.println(this + " 今天的任务结束");
		}

		@Override
		public String toString() {
			return String.format("门店：%-3d，制作机 %-3d 号", ToastStore.this.id, this.id);
		}
	}

	// 抹黄油
	class Butter implements Runnable {
		private final int id;
		private final BlockingQueue<Toast> driedQueue = ToastStore.this.driedQueue,
				butteredQueue = ToastStore.this.butteredQueue;

		public Butter(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			try {
				while (!ToastStore.this.isClosed()) {
					// 获取一个制作好的吐司，队列空则阻塞
					Toast toast = driedQueue.take();
					System.out.printf(this + " %-4s ：%s \n", "拿到", toast);
					// 模拟时间
					TimeUnit.MILLISECONDS.sleep(50 + random.nextInt(100));
					// 更新状态
					toast.status = ToastStatus.BUTTERED;
					ToastStore.this.print(this, "完成", toast);
					// 放入已抹黄油队列
					butteredQueue.put(toast);
				}
			} catch (InterruptedException e) {
				System.out.println(this + "中断");
			}
			System.out.println(this + " 今天的任务结束");
		}

		@Override
		public String toString() {
			return String.format("门店：%-3d，黄油机 %-3d 号", ToastStore.this.id, this.id);
		}
	}

	// 涂果酱
	class Jam implements Runnable {
		private final int id;
		private final BlockingQueue<Toast> butteredQueue = ToastStore.this.butteredQueue,
				finishedQueue = ToastStore.this.finishedQueue;

		public Jam(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			try {
				while (!ToastStore.this.isClosed()) {
					// 获取一个吐司，队列空则阻塞
					Toast toast = butteredQueue.take();
					System.out.printf(this + " %-4s ：%s \n", "拿到", toast);
					// 模拟时间
					TimeUnit.MILLISECONDS.sleep(50 + random.nextInt(100));
					// 更新状态
					toast.status = ToastStatus.JAMMED;
					ToastStore.this.print(this, "完成", toast);
					// 放入制作完成黄油队列
					finishedQueue.put(toast);
				}
			} catch (InterruptedException e) {
				System.out.println(this + "中断");
			}
			System.out.println(this + " 今天的任务结束");
		}

		@Override
		public String toString() {
			return String.format("门店：%-3d，果酱机 %-3d 号", ToastStore.this.id, this.id);
		}
	}
}

// 消费者
class Customer implements Runnable {
	private final int id;
	private final Random random = new Random(47);
	private final ToastStore toastStore;

	public Customer(int id, ToastStore toastStore) {
		this.id = id;
		this.toastStore = toastStore;
		this.toastStore.serving(this);
	}

	@Override
	public void run() {
		try {
			while (!this.toastStore.isClosed()) {
				ToastStore.Toast toast = this.toastStore.finishedQueue.take();
				this.toastStore.print(this, "取餐", toast);
				TimeUnit.MILLISECONDS.sleep(100 + random.nextInt(200));
				this.toastStore.print(this, "用餐", toast);
			}
		} catch (InterruptedException e) {
			System.out.println(this + "中断");
		}
		System.out.println(this + " 退出门店：" + this.toastStore.id);
	}

	@Override
	public String toString() {
		return String.format("门店：%-3d，顾客 %-5d 号", this.toastStore.id, this.id);
	}
}