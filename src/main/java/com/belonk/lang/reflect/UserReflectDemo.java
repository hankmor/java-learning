package com.belonk.lang.reflect;

import com.belonk.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by sun on 2021/11/8.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class UserReflectDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		UserReflectDemo demo = new UserReflectDemo();
		User user = demo.create();
		Assert.isTrue(user.equals(User.NULL));
		// 通过==判断getClass与class
		Assert.isTrue(user.getClass() == User.class);
		// 反射方法
		demo.setNameAndAge(user, "sam", 18);
		System.out.println(user);
		demo.getPublicMethods();
		demo.getAllMethods();
	}

	public User create() {
		try {
			// 获取类的字节码对象
			Class<?> userClass = Class.forName("com.belonk.lang.reflect.User");
			return (User) userClass.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setNameAndAge(User user, String name, int age) {
		try {
			Method setName = User.class.getMethod("setName", String.class);
			setName.invoke(user, name);
			Method setAge = User.class.getMethod("setAge", int.class);
			setAge.invoke(user, age);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void getPublicMethods() {
		System.out.println("==== getPublicMethods: ");
		Method[] methods = User.class.getMethods();
		Arrays.stream(methods).forEach(System.out::println);
	}

	public void getAllMethods() {
		System.out.println("==== getAllMethods: ");
		Method[] declaredMethods = User.class.getDeclaredMethods();
		Arrays.stream(declaredMethods).forEach(System.out::println);
	}
}