package com.belonk.lang.generic;

import java.util.List;

/**
 * Created by sun on 2021/11/14.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class WildcardGenericDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {

	}
}

class Clazz {

}

interface Interface {

}

class GenericClass<T> {

}

// 定义泛型类上边界
class GenericClass1<T extends Clazz> {

}

// 定义泛型类下边界: 编译错误
// class GenericClass2<T super Interface> {
// }

class GenericClass3<T extends Clazz> {
	// 泛型域
	private List<T> list;

	// 不能单独为域定于泛型类型：编译错误
	// 泛型域上边界
	// private <E> List<E extends T> list1;
	// 泛型域下边界
	// private <E> List<E super T> list2;

	// 通配符定义泛型上下边界
	private List<? extends T> list3;
	private List<? super T> list4;

	public <E> void method1(List<T> param) {
	}

	// 编译错误：方法参数不能定义泛型上下边界
	// public <E> void method2(List<E extends Clazz> param) {
	// }
	// public <E> void method2(List<E super Clazz> param) {
	// }

	// ok！通过通配符定义参数上下边界
	public <E> void method3(List<? extends Clazz> param, E e) {
	}

	public <E> void method4(List<? super Clazz> param, E e) {
	}
}