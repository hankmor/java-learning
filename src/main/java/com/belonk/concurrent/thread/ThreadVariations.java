package com.belonk.concurrent.thread;

import com.belonk.util.Printer;

// 内部类实现线程
class InnerThread1 {
	private int countDown = 5;
	private Inner inner;

	public InnerThread1(String name) {
		inner = new Inner(name);
	}

	private class Inner extends Thread {
		Inner(String name) {
			super(name);
			start(); // 启动线程
		}

		@Override
		public void run() {
			try {
				while (true) {
					Printer.println(this);
					if (--countDown == 0) return;
					sleep(10);
				}
			} catch (InterruptedException e) {
				Printer.println("interrupted");
			}
		}

		@Override
		public String toString() {
			return getName() + "：" + countDown;
		}
	}
}

// 匿名内部类创建线程
class InnerThread2 {
	private int countDown = 5;

	public InnerThread2(String name) {
		// 创建匿名线程
		new Thread(name) {
			@Override
			public void run() {
				while (true) {
					try {
						while (true) {
							Printer.println(this);
							if (--countDown == 0) return;
							sleep(10);
						}
					} catch (InterruptedException e) {
						Printer.println("interrupted");
					}
				}
			}

			@Override
			public String toString() {
				return getName() + "：" + countDown;
			}
		}.start();
	}
}

class InnerRunnable1 {
	private int countDown = 5;
	private Inner inner;

	public InnerRunnable1(String name) {
		inner = new Inner(name);
	}

	private class Inner implements Runnable {
		Thread t;

		Inner(String name) {
			t = new Thread(this, name);
			t.start();
		}

		@Override
		public void run() {
			try {
				while (true) {
					Printer.println(this);
					if (--countDown == 0) return;
					Thread.sleep(10);
				}
			} catch (InterruptedException e) {
				Printer.println("interrupted");
			}
		}

		@Override
		public String toString() {
			return t.getName() + "：" + countDown;
		}
	}
}

class InnerRunnable2 {
	private int countDown = 5;

	public InnerRunnable2(String name) {
		// 创建匿名线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						while (true) {
							Printer.println(this);
							if (--countDown == 0) return;
							Thread.sleep(10);
						}
					} catch (InterruptedException e) {
						Printer.println("interrupted");
					}
				}
			}

			@Override
			public String toString() {
				return Thread.currentThread().getName() + "：" + countDown;
			}
		}, name).start();
	}
}

class ThreadMethod {
	private int countDown = 5;
	private Thread t;
	private String name;

	public ThreadMethod(String name) {
		this.name = name;
	}

	public void runTask() {
		if (t == null) {
			t = new Thread(name) {
				@Override
				public void run() {
					while (true) {
						try {
							while (true) {
								Printer.println(this);
								if (--countDown == 0) return;
								sleep(10);
							}
						} catch (InterruptedException e) {
							Printer.println("interrupted");
						}
					}
				}

				@Override
				public String toString() {
					return Thread.currentThread().getName() + "：" + countDown;
				}
			};
		}
		t.start();
	}
}

/**
 * Created by sun on 2017/3/6.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class ThreadVariations {
	//~ Static fields/initializers =====================================================================================


	//~ Instance fields ================================================================================================


	//~ Constructors ===================================================================================================


	//~ Methods ========================================================================================================
	public static void main(String[] args) {
		// 线程实现的多种方式
		new InnerThread1("InnerThread1");
		new InnerThread2("InnerThread2");
		new InnerRunnable1("InnerRunnable1");
		new InnerRunnable2("InnerRunnable2");
		new ThreadMethod("ThreadMethod").runTask();
	}
}
