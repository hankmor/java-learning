package com.belonk.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/12/28.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class Restaurant {
	//~ Static fields/constants/initializer


	//~ Instance fields

	final ExecutorService executorService = Executors.newCachedThreadPool();
	// 厨师
	final Chef chef = new Chef(this);
	// 客人
	final WaitPerson waitPerson = new WaitPerson(this);
	// 菜品
	Meal meal;

	//~ Constructors

	public Restaurant() {
		executorService.execute(this.chef);
		executorService.execute(this.waitPerson);
	}

	//~ Methods

	/*
	 * 生产者消费案例：饭馆点餐，客人点餐后，等待初始做菜，厨师做好菜后等待取餐，假设饭馆只有一名厨师，每次只能做一份菜品。
	 *
	 * 案例中使用了两把锁：厨师和客人，厨师做菜的时候客人等待取餐，客人用餐的时候厨师等待点菜
	 */

	public static void main(String[] args) {
		new Restaurant();

		/*
		有人点餐...
		做好了 Meal 1
		客人取餐：Meal 1
		有人点餐...
		做好了 Meal 2
		客人取餐：Meal 2
		有人点餐...
		做好了 Meal 3
		客人取餐：Meal 3
		有人点餐...
		做好了 Meal 4
		客人取餐：Meal 4
		有人点餐...
		做好了 Meal 5
		客人取餐：Meal 5
		有人点餐...
		做好了 Meal 6
		客人取餐：Meal 6
		有人点餐...
		做好了 Meal 7
		客人取餐：Meal 7
		有人点餐...
		做好了 Meal 8
		客人取餐：Meal 8
		有人点餐...
		做好了 Meal 9
		客人取餐：Meal 9
		菜品卖光了...
		饭店关门了
		WaitPerson 被中断，饭店关门了
		 */
	}
}

/**
 * 资源类：菜品
 */
class Meal {
	//  编号
	private final int id;

	public Meal(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Meal " + this.id;
	}
}

class Chef implements Runnable {
	Restaurant restaurant;
	// 菜品数量
	private int count;

	public Chef(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				// 条件判断（千万别用if）：如果没有人取餐，等待
				// 这里的锁为厨师
				synchronized (this) {
					while (restaurant.meal != null) {
						this.wait();
					}
				}

				// 菜品达到上限，饭馆关门了
				if (++count == 10) {
					System.out.println("菜品卖光了...");
					this.restaurant.executorService.shutdownNow();
					break; // break的目的：不再接受点餐和做菜了
				}
				System.out.println("有人点餐...");

				// 等待客人来取餐。这里的锁为客人
				synchronized (this.restaurant.waitPerson) {
					this.restaurant.meal = new Meal(this.count);
					System.out.println("做好了 " + this.restaurant.meal);
					TimeUnit.MILLISECONDS.sleep(100);
					// 通知客人取餐
					this.restaurant.waitPerson.notifyAll();
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Chef工作被中断");
		}
		System.out.println("饭店关门了");
	}
}

class WaitPerson implements Runnable {
	Restaurant restaurant;

	public WaitPerson(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				// 还没有做好菜，等着。这里的锁为厨师
				synchronized (this) {
					while (this.restaurant.meal == null) {
						this.wait();
					}
				}

				System.out.println("客人取餐：" + this.restaurant.meal);

				// 等客人吃完，然后再通知厨师做菜
				synchronized (this.restaurant.chef) {
					TimeUnit.MILLISECONDS.sleep(100);
					// 菜品置为null
					this.restaurant.meal = null;
					this.restaurant.chef.notifyAll();
				}
			}
		} catch (InterruptedException e) {
			System.out.println("WaitPerson 被中断，饭店关门了");
		}
	}
}