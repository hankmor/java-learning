package com.belonk.concurrent.cas;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by sun on 2020/7/30.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class AtomicReferenceDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		User user1 = new User("zhangsan", 20);
		User user2 = new User("lisi", 25);
		// 初始值为null
		AtomicReference<User> userAtomicReference = new AtomicReference<>();
		boolean b = userAtomicReference.compareAndSet(null, user1);
		System.out.println("更新结果：" + b); // true
		b = userAtomicReference.compareAndSet(user1, user2);
		System.out.println("更新结果：" + b); // true
		b = userAtomicReference.compareAndSet(user1, user2);
		System.out.println("更新结果：" + b); // false
	}
}

class User {
	private String name;
	private int age;

	public User(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}