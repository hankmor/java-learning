package com.belonk.lang.generic;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by sun on 2021/11/7.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class ArrayCopier {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	// bad copy
	// 返回的是Object数组，不能强制转型
	public static Object[] badCopy(Object[] array, int length) {
		Object[] newArray = new Object[length];
		System.arraycopy(array, 0, newArray, 0, Math.min(array.length, length));
		return newArray;
	}

	// good copy
	// 返回的是数组，可以强制转型为原始对象数组
	public static Object goodCopy(Object array, int length) {
		Class<?> arrayClass = array.getClass();
		// 不是数组，直接返回null
		if (!arrayClass.isArray()) return null;

		Object newArray = Array.newInstance(arrayClass.getComponentType(), length);
		System.arraycopy(array, 0, newArray, 0, Math.min(Array.getLength(array), length));
		return newArray;
	}

	public static void main(String[] args) {
		int[] ints = {0, 1, 2, 3};
		// 编译失败:需要Object[]而不是int[]，不接受基本类型数组
		// Object[] objects = ArrayCopier.badCopy(ints, 10);

		Integer[] integers = {0, 1, 2, 3};
		Object[] objects = ArrayCopier.badCopy(integers, 10);
		System.out.println(Arrays.toString(objects));
		// 运行失败，ClassCastException: Object[]不能转换为Integer[]
		// integers = (Integer[]) objects;
		// System.out.println(Arrays.toString(integers));

		Object array = ArrayCopier.goodCopy(integers, 10);
		// ok
		integers = (Integer[]) array;
		System.out.println(Arrays.toString(integers));
	}
}