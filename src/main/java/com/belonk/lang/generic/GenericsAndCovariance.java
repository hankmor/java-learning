package com.belonk.lang.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Fruit {
}

class Apple extends Fruit {
}

class Jonathan extends Apple {
}

class Orange extends Fruit {
}

/**
 * Created by sun on 2018/9/12.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class GenericsAndCovariance {
	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Static fields/constants/initializer
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */



	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Instance fields
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */



	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Constructors
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */



	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Public Methods
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */

	public static void main(String[] args) {
		GenericsAndCovariance gac = new GenericsAndCovariance();
		gac.concreteGenericType();
		gac.extendGenericType();
	}

	public void concreteGenericType() {
		List<Fruit> fList = new ArrayList<>();
		fList.add(new Apple());
		fList.add(new Fruit());
		fList.add(null);
		// 编译失败
		// fList.add(new Object());
	}

	public void extendGenericType() {
		List<? extends Fruit> fList = new ArrayList<Apple>();
		// 编译失败：不能添加具体的对象
		// fList.add(new Apple());
		// fList.add(new Fruit());
		// ok，可以添加null
		fList.add(null);

		writeExtend(fList);
		writeExtendGeneric(fList, new Apple());
		writeExtendGeneric(fList, new Fruit());

		fList = Arrays.asList(new Apple(), new Apple());
		Apple a = (Apple) fList.get(0);
		// ok，因为这两个方式传递的都是Object参数，而不是泛型参数
		fList.contains(new Apple());
		fList.indexOf(new Apple());
	}

	private void writeExtend(List<? extends Fruit> fruits) {
		// 编译失败，作为方法参数也不能写入具体对象
		// fruits.add(new Apple());
		// fruits.add(new Fruit());
	}

	private <T> void writeExtendGeneric(List<? extends T> list, T item) {
		// 编译失败，类型不匹配
		// list.add(item);
	}

	public void superGenericType() {
		List<? super Jonathan> fList = new ArrayList<>();
		// 编译失败，同样不能写入具体对象
		// fList.add(new Apple());
		// fList.add(new Fruit());
		// ok，可以写入null
		fList.add(null);

		writeSuper(fList);

		List<? super Apple> apples = new ArrayList<>();
		List<? super Fruit> fruits = new ArrayList<>();

		writeSuperGeneric(fList, new Jonathan());
		// 编译失败，下届不能存储夫类型，即：Jonathan或其父类不能存储Apple
		// writeSuperGeneric(fList, new Apple());
		writeSuperGeneric(apples, new Apple());
		writeSuperGeneric(fruits, new Fruit());
		// ok，上届可以存储子类型对象，Fruit或其父类当然可以存储其子类
		writeSuperGeneric(fruits, new Apple());
	}

	private void writeSuper(List<? super Jonathan> objects) {
		objects.add(new Jonathan());
		// 编译错误，不能写入父类对象
		// objects.add(new Apple());
		// objects.add(new Fruit());
	}

	private <T> void writeSuperGeneric(List<? super T> list, T item) {
		// ok，可以写入，super
		list.add(item);
	}

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Private Methods
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */


}