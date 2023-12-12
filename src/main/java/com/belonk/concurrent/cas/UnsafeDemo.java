//package com.belonk.concurrent.cas;
//
//import sun.misc.Unsafe;
//
//import java.lang.reflect.Field;
//
///**
// * Created by sun on 2020/7/28.
// *
// * @author sunfuchang03@126.com
// * @since 1.0
// */
//public class UnsafeDemo {
//	//~ Static fields/constants/initializer
//
//
//	//~ Instance fields
//
//
//	//~ Constructors
//
//
//	//~ Methods
//
//	/**
//	 * Unsafe类为一单例实现，提供静态方法getUnsafe获取Unsafe实例，当且仅当调用getUnsafe方法的类为引导类加载器(Bootstrap Classloader)
//	 * 所加载时才合法，否则抛出SecurityException异常。
//	 * 解决方案有两种：
//	 * 1、将使用Unsafe的jar包放到bootstrap路径上，java -Xbootclasspath/a: ${path} path为jar包路径
//	 * 2、使用反射获取Unsafe实例
//	 */
//	public static void main(String[] args) throws NoSuchFieldException {
//		// 不能直接获取，因为只能引导类加载器(Bootstrap Classloader)加载的类才能使用
//		// Unsafe unsafe = Unsafe.getUnsafe();
//		// 使用反射获取Unsafe类
//		Unsafe unsafe = getUnsafe();
//		assert unsafe != null;
//		ForUnsafe forUnsafe = new ForUnsafe(100, 200);
//		System.out.println("forUnsafe: " + forUnsafe.toString());
//		// valueOffset：内存地址中的偏移量
//		long valueOffsetA = unsafe.objectFieldOffset(forUnsafe.getClass().getDeclaredField("a"));
//		long valueOffsetB = unsafe.objectFieldOffset(forUnsafe.getClass().getDeclaredField("b"));
//		// offset a:12
//		// offset b:16
//		System.out.println("属性a在内存中的偏移量：" + valueOffsetA);
//		System.out.println("属性b在内存中的偏移量：" + valueOffsetB);
//		System.out.println("=== CAS修改一次 ===");
//		System.out.println("修改前：" + forUnsafe);
//		boolean b1 = unsafe.compareAndSwapInt(forUnsafe, valueOffsetA, 100, 101);
//		boolean b2 = unsafe.compareAndSwapInt(forUnsafe, valueOffsetB, 200, 201);
//		System.out.println("属性a修改结果：" + b1); // true
//		System.out.println("属性b修改结果：" + b2); // true
//		System.out.println("修改后：" + forUnsafe);
//		System.out.println("=== CAS再修改一次 ===");
//		boolean b3 = unsafe.compareAndSwapInt(forUnsafe, valueOffsetA, 101, 102);
//		boolean b4 = unsafe.compareAndSwapInt(forUnsafe, valueOffsetB, 200, 202);
//		System.out.println("属性a修改结果：" + b3); // true
//		System.out.println("属性b修改结果：" + b4); // false
//		System.out.println("修改后：" + forUnsafe);
//	}
//
//	private static Unsafe getUnsafe() {
//		try {
//			// theUnsafe: private static final Unsafe theUnsafe，存储了Unsafe的实例对象
//			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
//			theUnsafe.setAccessible(true);
//			return (Unsafe) theUnsafe.get(null); // 静态属性的get方法，返回实例对象
//		} catch (NoSuchFieldException | IllegalAccessException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//}
//
//class ForUnsafe {
//	private int a;
//	private int b;
//
//	public ForUnsafe(int a, int b) {
//		this.a = a;
//		this.b = b;
//	}
//
//	public int getA() {
//		return a;
//	}
//
//	public void setA(int a) {
//		this.a = a;
//	}
//
//	public int getB() {
//		return b;
//	}
//
//	public void setB(int b) {
//		this.b = b;
//	}
//
//	@Override
//	public String toString() {
//		return "ForUnsafe{" +
//				"a=" + a +
//				", b=" + b +
//				'}';
//	}
//}