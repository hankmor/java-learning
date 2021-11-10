package com.belonk.lang.reflect;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by sun on 2021/11/10.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class ArrayReflectDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		modifyArray();
		createArray();
	}

	public static void createArray() {
		// 创建指定对象的数组
		Item[] items = (Item[]) Array.newInstance(Item.class, 1);
		items[0] = new Item();
		Object o = Array.get(items, 0);
		System.out.println(o);

		// 创建基本类型数组
		int[] intArray = (int[]) Array.newInstance(int.class, 1);
		intArray[0] = -1;
		int anInt = Array.getInt(intArray, 0);
		System.out.println(anInt);

		// 创建二维数组
		int[][] ints = (int[][]) Array.newInstance(int.class, 2, 2);
		ints[0][0] = 0;
		ints[0][1] = 1;
		ints[1][0] = 2;
		ints[1][1] = 3;
		System.out.println(Arrays.deepToString(ints));
	}

	public static void modifyArray() {
		int[] array = new int[1];
		array[0] = 1;
		int intItem = Array.getInt(array, 0);
		System.out.println(intItem);
		System.out.println(Array.getLength(array));

		Object[] objects = new Object[4];
		objects[0] = 1;
		objects[1] = "a";
		objects[2] = 2L;
		objects[3] = 0.1f;

		System.out.println(Array.getLength(objects));
		Object o = Array.get(objects, 1);
		System.out.println(o);
		// IllegalArgumentException: Argument is not an array
		// int anInt = Array.getInt(objects, 0);
		// System.out.println(anInt);
		// IllegalArgumentException: Argument is not an array
		// float aFloat = Array.getFloat(objects, 3);
		// System.out.println(aFloat);

		Array.set(objects, 0, new Item());
		Item item = (Item) Array.get(objects, 0);
		System.out.println(item);

		// IllegalArgumentException: Argument is not an array
		// Array.setInt(objects, 0, 100);
	}
}

class Item {

}