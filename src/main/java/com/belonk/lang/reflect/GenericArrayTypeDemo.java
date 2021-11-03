package com.belonk.lang.reflect;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * GenericArrayType表示泛型数组类型，它只有一个方法：
 * getGenericComponentType：返回数组的泛型类型。
 */
public class GenericArrayTypeDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		GenericArrayTypeDemo genericArrayTypeDemo = new GenericArrayTypeDemo();
		genericArrayTypeDemo.test();
	}

	public void test() {
		try {
			// 泛型数组，类型为GenericArrayType, 通过getGenericComponentType可以获取其泛型类型（TypeVariable、GenericArrayType、ParameterizedType等）

			// T[]：泛型数组，类型是一个TypeVariable，类型名称为T
			Type genericType = GenericArrayTypeClass.class.getDeclaredField("array").getGenericType();
			assert genericType instanceof GenericArrayType;
			GenericArrayType genericArrayType = (GenericArrayType) genericType;
			System.out.println(genericArrayType.getGenericComponentType());
			System.out.println(genericArrayType.getGenericComponentType().getClass());

			// T[][]：二维数组，类型仍然是GenericArrayType
			genericArrayType = (GenericArrayType) GenericArrayTypeClass.class.getDeclaredField("array1").getGenericType();
			System.out.println(genericArrayType.getGenericComponentType());
			System.out.println(genericArrayType.getGenericComponentType().getClass());

			// 参数化类型数组
			genericArrayType = (GenericArrayType) GenericArrayTypeClass.class.getDeclaredField("listArray").getGenericType();
			System.out.println(genericArrayType.getGenericComponentType());
			System.out.println(genericArrayType.getGenericComponentType().getClass());

			// 参数化类型数组
			genericArrayType = (GenericArrayType) GenericArrayTypeClass.class.getDeclaredField("numberArray").getGenericType();
			System.out.println(genericArrayType.getGenericComponentType());
			System.out.println(genericArrayType.getGenericComponentType().getClass());

			// 非泛型数组，类型是Class
			Type array0 = GenericArrayTypeClass.class.getDeclaredField("array0").getGenericType();
			System.out.println(array0.getClass());
			/*
			T
			class sun.reflect.generics.reflectiveObjects.TypeVariableImpl
			T[]
			class sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl
			java.util.List<T>
			class sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
			java.util.List<? extends java.lang.Number>
			class sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
			class java.lang.Class
			 */
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
}

class GenericArrayTypeClass<T> {
	private String[] array0;
	private T[] array;
	private T[][] array1;
	private List<T>[] listArray;
	private List<? extends Number>[] numberArray;
}

