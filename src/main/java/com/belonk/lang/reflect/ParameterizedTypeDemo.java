package com.belonk.lang.reflect;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

/**
 * ParameterizedType: 参数化类型，指的就是整个泛型定义。它有三个方法：
 * 1、getActualTypeArguments：获取泛型定义的实际的类型，返回数组
 * 2、getRawType：返回原始类型
 * 3、getOwnerType：返回当前类型作为成员（内部类）的父类型
 */
public class ParameterizedTypeDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields

	// 泛型属性
	private List<String> list;

	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		ParameterizedTypeDemo parameterizedTypeDemo = new ParameterizedTypeDemo();
		parameterizedTypeDemo.testGenericField();
		parameterizedTypeDemo.testGenericClass();
		parameterizedTypeDemo.testGenericMethod();
	}

	public void testGenericField() {
		System.out.println("==== 测试泛型属性：");
		try {
			Field field = ParameterizedTypeDemo.class.getDeclaredField("list");
			// 获取泛型类型，这里返回的就是ParameterizedTypeImpl，这是ParameterizedType接口的默认实现
			Type genericType = field.getGenericType();
			System.out.println("原始类型：" + field.getType());
			assert genericType instanceof ParameterizedType;
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			// 输出属性对应的泛型类型
			System.out.println("泛型类型：" + Arrays.toString(parameterizedType.getActualTypeArguments()));
			// 获取原始类型，结果同field.getType()
			System.out.println("原始类型：" + parameterizedType.getRawType());
			System.out.println("所有者类型：" + parameterizedType.getOwnerType());
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		/*
		原始类型：interface java.util.List
		泛型类型：[class java.lang.String]
		原始类型：interface java.util.List
		所有者类型：null
		 */
	}

	// 泛型父类
	class TopParameterizedType<T> {

	}

	// 内部类，其泛型由父类持有，为String类型
	class InnerParameterizedType extends TopParameterizedType<String> {

	}

	public void testGenericClass() {
		System.out.println("==== 测试泛型类：");
		// getGenericSuperclass方法可以拿到泛型父类，从而获取父类上定义的泛型类型
		Type genericSuperclass = InnerParameterizedType.class.getGenericSuperclass();
		// 泛型父类类型是ParameterizedType
		assert genericSuperclass instanceof ParameterizedType;
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		System.out.println("泛型类型：" + Arrays.toString(parameterizedType.getActualTypeArguments()));
		System.out.println("原始类型：" + parameterizedType.getRawType());
		// 由于InnerParameterizedType是ParameterizedTypeDemo的内部类，所以所有者是ParameterizedTypeDemo
		System.out.println("所有者类型：" + parameterizedType.getOwnerType());
		/*
		泛型类型：[class java.lang.String]
		原始类型：class com.belonk.lang.reflect.ParameterizedTypeDemo$TopParameterizedType
		所有者类型：class com.belonk.lang.reflect.ParameterizedTypeDemo
		 */
	}

	// 泛型方法声明
	// 泛型方法并没有定义具体的泛型类型
	private <T> void genericMethod(List<T> list) {
	}

	public void testGenericMethod() {
		System.out.println("==== 测试泛型方法：");
		try {
			Method genericMethod = ParameterizedTypeDemo.class.getDeclaredMethod("genericMethod", List.class);
			Parameter parameter = genericMethod.getParameters()[0];
			ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
			System.out.println("泛型类型：" + Arrays.toString(parameterizedType.getActualTypeArguments()));
			System.out.println("原始类型：" + parameterizedType.getRawType());
			System.out.println("所有者类型：" + parameterizedType.getOwnerType());
			/*
			泛型类型：[T]
			原始类型：interface java.util.List
			所有者类型：null
			 */
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
}