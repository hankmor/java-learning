package com.belonk.concurrent.future;

import java.util.concurrent.*;

/**
 * Future测试
 *
 * @author belonk.com
 * @since 1.0
 */
public class FutureTest {
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		System.out.println("Starting ...");

		ExecutorService executorService = Executors.newCachedThreadPool();

		int num = 10;
		FutureTask<Long> sumTask = new FutureTask<>(new Sum(num));
		executorService.submit(sumTask);

		FutureTask<Double> calcXbTask = new FutureTask<>(new CalcXb(3, 4));
		executorService.submit(calcXbTask);

		FutureTask<Long> jcTask = new FutureTask<>(new Factorial(num));
		executorService.submit(jcTask);

		FutureTask<Long> longTimeTask = new FutureTask<Long>(new SpendLongTimeTask());
		executorService.submit(longTimeTask);

		System.out.println("Do something else ...");

		Long result = sumTask.get();
		System.out.println("Sum result : " + result);

		Double xb = calcXbTask.get();
		System.out.println("CalcXb result : " + xb);

		result = jcTask.get();
		System.out.println("Jc result : " + result);

		try {
			// 超时抛出异常，并返回
			result = longTimeTask.get(5, TimeUnit.SECONDS);
			System.out.println("LongTimeTask result : " + result);
		} catch (TimeoutException e) {
			e.printStackTrace();
			System.out.println("Cancel task ...");
			// 立即中断任务
			// longTimeTask.cancel(true);
			// 等待任务执行完成
			longTimeTask.cancel(false);
		}

		System.out.println("End ...");
		executorService.shutdown();
	}
}

/**
 * 计算小于等于某数的所有正整数的和
 */
class Sum implements Callable<Long> {
	private int stop;

	Sum(int num) {
		this.stop = num;
	}

	@Override
	public Long call() throws Exception {
		System.out.println("Sum is calling ...");
		Long sum = 0L;
		for (int i = 1; i <= stop; i++) {
			sum += i;
		}
		Thread.sleep(2000);
		System.out.println("Sum called success ...");
		return sum;
	}
}

/**
 * 计算直角三角形斜边长度
 */
class CalcXb implements Callable<Double> {
	private int a;
	private int b;

	CalcXb(int a, int b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public Double call() throws Exception {
		System.out.println("CalcXb is calling ...");
		Double c = Math.sqrt(a * a + b * b);
		Thread.sleep(2000);
		System.out.println("CalcXb called success ...");
		return c;
	}
}

/**
 * 阶乘计算
 */
class Factorial implements Callable<Long> {
	private int stop;

	Factorial(int num) {
		this.stop = num;
	}

	@Override
	public Long call() throws Exception {
		System.out.println("Factorial is calling ...");
		Long result = 1L;
		for (int i = 2; i <= stop; i++) {
			result *= i;
		}
		Thread.sleep(2000);
		System.out.println("Factorial called success ...");
		return result;
	}
}

/**
 * 长耗时任务
 */
class SpendLongTimeTask implements Callable<Long> {
	@Override
	public Long call() throws Exception {
		System.out.println("SpendLongTimeTask is calling ...");
		Thread.sleep(8000);
		System.out.println("SpendLongTimeTask called success ...");
		return 10000L;
	}
}