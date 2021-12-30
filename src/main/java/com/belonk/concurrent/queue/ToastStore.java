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
	 * 制作机器数量
	 */
	private int makeMachineNumber;
	/**
	 * 抹黄油的机器数量
	 */
	private int butterMachineNumber;
	/**
	 * 涂果酱的机器数量
	 */
	private int jamMachineNumber;
	/**
	 * 最大的吐司数量
	 */
	private static final int maxToast = 10;
	private final ExecutorService executorService = Executors.newCachedThreadPool();
	private final BlockingQueue<Toast> driedQueue = new LinkedBlockingQueue<>();
	private final BlockingQueue<Toast> butteredQueue = new LinkedBlockingQueue<>();
	private final BlockingQueue<Toast> finishedQueue = new LinkedBlockingQueue<>();
	private volatile boolean closed;
	private volatile int totalNumber;

	//~ Constructors

	public ToastStore() {

	}

	public ToastStore(int makeMachineNumber, int butterMachineNumber, int jamMachineNumber) {
		this.makeMachineNumber = makeMachineNumber;
		this.butterMachineNumber = butterMachineNumber;
		this.jamMachineNumber = jamMachineNumber;
	}


	//~ Methods

	/*
	 * 自动吐司面包制作流程案例：吐司面包需要制作、抹黄油、涂果酱三个步骤，使用阻塞队列实现3个步骤的对象流转。
	 *
	 * 一共需要三个队列来盛放吐司：刚出炉的、已抹黄油的、制作完成的
	 */

	public static void main(String[] args) throws InterruptedException {
		// 开店
		ToastStore toastStore = new ToastStore(3, 2, 1);
		// 开业了
		toastStore.open();

		// TimeUnit.SECONDS.sleep(5);

		// 顾客创建线程
		// int customerNumber = 10;
		// ExecutorService executorService = Executors.newCachedThreadPool();
		// for (int i = 0; i < customerNumber; i++) {
		// 	executorService.execute(new Customer(i, toastStore));
		// }
		// executorService.shutdown();
	}

	/**
	 * 开店营业了
	 */
	public void open() {
		closed = false;
		// 制作机器
		for (int i = 0; i < makeMachineNumber; i++) {
			executorService.execute(new ToastMaking(i));
		}
		for (int i = 0; i < butterMachineNumber; i++) {
			// 抹黄油的机器
			executorService.execute(new Butter(i));
		}
		for (int i = 0; i < jamMachineNumber; i++) {
			// 涂果酱的机器
			executorService.execute(new Jam(i));
		}

	}

	/**
	 * 来顾客了，提供优质服务
	 */
	public void serving(Customer customer) {
		System.out.println("欢迎您，" + customer);
		executorService.execute(customer);
	}

	public boolean needClose() {
		if (totalNumber > maxToast) {
			System.out.println("吐司数量已达上限，打烊了: " + totalNumber);
			this.close();
			return true;
		} else {
			++totalNumber;
			return false;
		}
	}

	/**
	 * 打烊了
	 */
	public void close() {
		closed = true;
		executorService.shutdown();
	}

	public void setMakeMachineNumber(int makeMachineNumber) {
		this.makeMachineNumber = makeMachineNumber;
	}

	public void setButterMachineNumber(int butterMachineNumber) {
		this.butterMachineNumber = butterMachineNumber;
	}

	public void setJamMachineNumber(int jamMachineNumber) {
		this.jamMachineNumber = jamMachineNumber;
	}

	public boolean isClosed() {
		return closed;
	}

	// 吐司状态：刚制作、抹黄油、涂果酱
	enum ToastStatus {
		DRY, BUTTERED, JAMMED;
	}

	static class Toast {
		static int count = 0;
		// 这里的id生成在多线程并发时会重复，因为是非原子操作
		// final int id = ++needClose;
		final int id;
		ToastStatus status = ToastStatus.DRY;

		public Toast() {
			// 想让每台机器制作的吐司都有唯一的id，即id不重复，需要加锁
			synchronized (Toast.class) {
				this.id = ++count;
			}
		}

		@Override
		public String toString() {
			return "Toast " + id + "(" + status + ")";
		}
	}

	class ToastMaking implements Runnable {
		private final int id;
		private final BlockingQueue<Toast> driedQueue = ToastStore.this.driedQueue;
		private final Random random = new Random(47);

		public ToastMaking(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			try {
				while (!ToastStore.this.isClosed()) {
					// TODO 数量没有同步
					synchronized (ToastStore.class) {
						while (ToastStore.this.needClose()) {
							return;
						}
					}
					// 模拟制作时间
					TimeUnit.MILLISECONDS.sleep(50 + random.nextInt(200));
					Toast toast = new Toast();
					System.out.println(this + " 完成：" + toast);

					// 放入队列，队列满则阻塞
					driedQueue.put(toast);
				}
			} catch (InterruptedException e) {
				System.out.println(this + "中断");
			}
		}

		@Override
		public String toString() {
			return "ToastMaking " + this.id + " 号机";
		}
	}

	// 抹黄油
	class Butter implements Runnable {
		private final int id;
		private final BlockingQueue<Toast> driedQueue = ToastStore.this.driedQueue,
				butteredQueue = ToastStore.this.butteredQueue;
		private final Random random = new Random(47);

		public Butter(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			try {
				while (!ToastStore.this.isClosed()) {
					// 获取一个制作好的吐司，队列空则阻塞
					Toast toast = driedQueue.take();
					System.out.println(this + "拿到：" + toast);
					// 模拟时间
					TimeUnit.MILLISECONDS.sleep(50 + random.nextInt(100));
					// 更新状态
					toast.status = ToastStatus.BUTTERED;
					System.out.println(this + "完成: " + toast);
					// 放入已抹黄油队列
					butteredQueue.put(toast);
				}
			} catch (InterruptedException e) {
				System.out.println(this + "中断");
			}
		}

		@Override
		public String toString() {
			return "Butter " + this.id + " 号机";
		}
	}

	// 涂果酱
	class Jam implements Runnable {
		private final int id;
		private final BlockingQueue<Toast> butteredQueue = ToastStore.this.butteredQueue,
				finishedQueue = ToastStore.this.finishedQueue;
		private final Random random = new Random(47);

		public Jam(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			try {
				while (!ToastStore.this.isClosed()) {
					// 获取一个吐司，队列空则阻塞
					Toast toast = butteredQueue.take();
					System.out.println(this + "拿到：" + toast);
					// 模拟时间
					TimeUnit.MILLISECONDS.sleep(50 + random.nextInt(100));
					// 更新状态
					toast.status = ToastStatus.JAMMED;
					System.out.println(this + "完成: " + toast);
					// 放入制作完成黄油队列
					finishedQueue.put(toast);
				}
			} catch (InterruptedException e) {
				System.out.println(this + "中断");
			}
		}

		@Override
		public String toString() {
			return "Jam " + this.id + " 号机";
		}
	}


	// 消费者
	class Customer implements Runnable {
		private final int id;
		private BlockingQueue<Toast> finishedQueue;
		private final Random random = new Random(47);

		public Customer(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			try {
				while (!ToastStore.this.isClosed()) {
					Toast toast = finishedQueue.take();
					System.out.println(this + "取餐：" + toast);
					TimeUnit.MILLISECONDS.sleep(100 + random.nextInt(200));
					System.out.println(this + "用餐：" + toast);
				}
			} catch (InterruptedException e) {
				System.out.println(this + "中断");
			}
		}

		@Override
		public String toString() {
			return "Customer " + this.id + " 号顾客";
		}
	}
}