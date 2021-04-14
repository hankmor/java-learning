package com.belonk.lang.val;

/**
 * Created by sun on 2020/4/26.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ValueAndReferenceTransport {
	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Static fields/constants/initializer
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */



	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Instance fields
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */



	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Constructors
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */



	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Methods
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */

	public int val() {
		int a = 1;
		try {
			// a为基础类型，存放在jvm栈中
			// return a时有finally，先将a的值缓存起来，然后执行finally，最后返回缓存起来的a的值
			return a;
		} finally {
			a = 3;
		}
	}

	public void ref() {
		// 栈中存放的是user的引用

		// 栈中开辟控件存放user的引用，指向堆中的User实例
		User user = new User("aaa");
		// 栈中开辟控件存放方法参数User的引用user2，并将user复制到user2
		handleUser(user);
		// 整个过程user1指向的User对象实例的name属性始终是aaa
		System.out.println(user.getName());
	}

	public void handleUser(User user2) {
		// 栈中开辟控件存放user3引用，指向一个新的User对象
		User user3 = new User("bbb");
		// user3引用复制给user2
		user2 = user3;
		// user2改变名称，其实更改的是user3引用指向的User实例名称，原user2不变
		// 如果注释掉前边两行代码，那么users指向的实例是引用类型，所以user2的name会被改为ccc
		user2.setName("ccc");
	}

	public static void main(String[] args) {
		ValueAndReferenceTransport vart = new ValueAndReferenceTransport();
		// 输出1
		System.out.println(vart.val());
		// 输出aaa
		vart.ref();
	}
}

class User {
	public User(String name) {
		this.name = name;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
