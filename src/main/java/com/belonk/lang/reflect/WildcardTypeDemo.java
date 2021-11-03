package com.belonk.lang.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * WildcardType表示泛型的通配符，包括两个方法：
 * 1、getUpperBounds：获取上边界
 * 2、getLowerBounds：获取下边界
 */
public class WildcardTypeDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ MethodsWildcardType

	public static void main(String[] args) {
		WildcardTypeDemo wildcardTypeDemo = new WildcardTypeDemo();
		wildcardTypeDemo.testGenericField();
		wildcardTypeDemo.testGenericConstructor();
		wildcardTypeDemo.testGenericMethod();
	}

	public void testGenericField() {
		System.out.println("==== testGenericField: ");
		try {
			Field numberList = WildcardTypeClass.class.getDeclaredField("numberList");
			// 获取泛型类型
			Type genericType = numberList.getGenericType();
			assert genericType instanceof ParameterizedType;
			// 获取到ParameterizedType
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			// 实际的泛型类型是WildcardType
			Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
			assert actualTypeArgument instanceof WildcardType;
			WildcardType wildcardType = (WildcardType) actualTypeArgument;
			System.out.println(Arrays.toString(wildcardType.getUpperBounds()));
			System.out.println(Arrays.toString(wildcardType.getLowerBounds()));

			parameterizedType = (ParameterizedType) WildcardTypeClass.class.getDeclaredField("intList").getGenericType();
			wildcardType = (WildcardType) parameterizedType.getActualTypeArguments()[0];
			System.out.println(Arrays.toString(wildcardType.getUpperBounds()));
			System.out.println(Arrays.toString(wildcardType.getLowerBounds()));
			/*
			[class java.lang.Number]
			[]
			[class java.lang.Object]
			[class java.lang.Integer]
			 */
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public void testGenericConstructor() {
		System.out.println("=== testGenericConstructor: ");
		// 获取到泛型参数
		ParameterizedType parameterizedType = (ParameterizedType) WildcardTypeClass.class.getConstructors()[0].getGenericParameterTypes()[0];
		WildcardType wildcardType = (WildcardType) parameterizedType.getActualTypeArguments()[0];
		System.out.println(Arrays.toString(wildcardType.getUpperBounds()));
		System.out.println(Arrays.toString(wildcardType.getLowerBounds()));

		parameterizedType = (ParameterizedType) WildcardTypeClass.class.getConstructors()[1].getGenericParameterTypes()[0];
		wildcardType = (WildcardType) parameterizedType.getActualTypeArguments()[0];
		System.out.println(Arrays.toString(wildcardType.getUpperBounds()));
		System.out.println(Arrays.toString(wildcardType.getLowerBounds()));
		/*
		[class java.lang.Number]
		[]
		[class java.lang.Object]
		[class java.lang.Number]
		 */
	}

	public void testGenericMethod() {
		System.out.println("==== testGenericMethod: ");
		try {
			// TODO WildcardTypeClass.class.getMethods()顺序与定义的不一致
			// 获取泛型参数
			ParameterizedType parameterizedType = (ParameterizedType) WildcardTypeClass.class.getMethod("method", List.class).getGenericParameterTypes()[0];
			WildcardType wildcardType = (WildcardType) parameterizedType.getActualTypeArguments()[0];
			System.out.println(Arrays.toString(wildcardType.getUpperBounds()));
			System.out.println(Arrays.toString(wildcardType.getLowerBounds()));

			parameterizedType = (ParameterizedType) WildcardTypeClass.class.getMethod("method", Set.class).getGenericParameterTypes()[0];
			wildcardType = (WildcardType) parameterizedType.getActualTypeArguments()[0];
			System.out.println(Arrays.toString(wildcardType.getUpperBounds()));
			System.out.println(Arrays.toString(wildcardType.getLowerBounds()));

			/*
			[class java.lang.Number]
			[]
			[class java.lang.Object]
			[class java.lang.Integer]
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class WildcardTypeClass {
	private List<? extends Number> numberList;
	private List<? super Integer> intList;

	public WildcardTypeClass(List<? extends Number> numberList) {
	}

	public WildcardTypeClass(Set<? super Number> intSet) {
	}

	public void method(List<? extends Number> numberList) {

	}

	public void method(Set<? super Integer> intList) {

	}
}