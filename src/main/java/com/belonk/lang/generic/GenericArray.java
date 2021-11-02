package com.belonk.lang.generic;

import com.belonk.json.fastjson.Obj;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Java如何创建泛型数组？
 * <p>
 * Created by sun on 2021/11/2.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class GenericArray {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		GenericArray1<String> genericArray1 = new GenericArray1<>(10);
		genericArray1.add("item1");
		genericArray1.add("item2");
		// 编译失败, 不能添加int元素, 泛型起到了限定数组元素的作用
		// genericArray1.add(1);
		// 运行时错误, ClassCastException, 方法返回的其实是Object[], 转型为String[]失败
		// String[] array = genericArray1.array();
		// ok
		Object[] objectArray = genericArray1.objectArray();

		GenericArray2<String> genericArray2 = new GenericArray2<>(10);
		genericArray2.add("item1");
		genericArray2.add("item2");
		// 编译错误: 元素必须为String
		// genericArray2.add(1);
		// ok
		String item1 = genericArray2.get(0);
		// ok
		Object[] array = genericArray2.array();

		GenericArray3<String> genericArray3 = new GenericArray3<>(String.class, 10);
		genericArray3.add("item1");
		genericArray3.add("item2");
		// ok
		String item = genericArray3.get(0);
		// ok
		String[] stringArray = genericArray3.array();
	}
}

/**
 * 试图强转来创建泛型数组，但是由于泛型类型被擦除，所以实际上创建的是Object[]，所以强转失败。
 *
 * @param <T>
 */
class GenericArray1<T> {
	private final T[] array;
	private int index;

	@SuppressWarnings("unchecked")
	public GenericArray1(int size) {
		assert size >= 0;
		this.array = (T[]) new Object[size]; // ClassCastException: Object数组不能转为String数组
	}

	public void add(T item) {
		array[index++] = item;
	}

	public T[] array() {
		return array;
	}

	public Object[] objectArray() {
		return array;
	}
}

/**
 * 用一个Object[]来存储元素, 泛型限制添加元素的类型, get方法返回泛型类型.
 *
 * @param <T>
 */
class GenericArray2<T> {
	private final Object[] array;
	private int index;

	public GenericArray2(int size) {
		assert size >= 0;
		this.array = new Object[size];
	}

	public void add(T item) {
		array[index++] = item;
	}

	@SuppressWarnings("unchecked")
	public T get(int idx) {
		return (T) this.array[idx];
	}

	public Object[] array() {
		return array;
	}
}

/**
 * 通过Array.newInstance方法创建泛型数组.
 *
 * @param <T>
 */
class GenericArray3<T> {
	private final T[] array;
	private int index;

	@SuppressWarnings("unchecked")
	public GenericArray3(Class<T> type, int index) {
		this.array = (T[]) Array.newInstance(type, index); // 创建指定类型的数组
	}

	public void add(T item) {
		array[index++] = item;
	}

	public T get(int idx) {
		return this.array[idx];
	}

	public T[] array() {
		return array;
	}
}