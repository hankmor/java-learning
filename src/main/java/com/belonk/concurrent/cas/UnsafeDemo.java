package com.belonk.concurrent.cas;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by sun on 2020/7/28.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class UnsafeDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/**
	 * Unsafe类为一单例实现，提供静态方法getUnsafe获取Unsafe实例，当且仅当调用getUnsafe方法的类为引导类加载器(Bootstrap Classloader)
	 * 所加载时才合法，否则抛出SecurityException异常。
	 * 解决方案有两种：
	 * 1、将使用Unsafe的jar包放到bootstrap路径上，java -Xbootclasspath/a: ${path} path为jar包路径
	 * 2、使用反射获取Unsafe实例
	 */
	public static void main(String[] args) throws NoSuchFieldException {
		// 不能直接获取，因为只能引导类加载器(Bootstrap Classloader)加载的类才能使用
		// Unsafe unsafe = Unsafe.getUnsafe();
		// 使用反射获取Unsafe类
		Unsafe unsafe = getUnsafe();
		ForUnsafe forUnsafe = new ForUnsafe();
		System.out.println("forUnsafe: " + forUnsafe.toString());
		// valueOffset
		long valueOffsetA = unsafe.objectFieldOffset(forUnsafe.getClass().getDeclaredField("a"));
		long valueOffsetB = unsafe.objectFieldOffset(forUnsafe.getClass().getDeclaredField("b"));
		// offset a:12
		// offset b:16
		System.out.println("offset a:" + valueOffsetA);
		System.out.println("offset b:" + valueOffsetB);
	}

	private static Unsafe getUnsafe() {
		try {
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			return (Unsafe) theUnsafe.get(null);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}

class ForUnsafe {
	private int a;
	private int b;

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}
}