package com.belonk.lang.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sun on 2021/11/1.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class GenericActualType {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		/*
		 *
		 * 获取泛型类型：
		 * 1、class.getGenericSuperclass(): 返回表示此Class表示的实体（类、接口、原始类型或 void）的直接超类的Type，如果它是
		 * ParameterizedType实例，则可以通过它获取泛型类型。
		 * 2、class.getTypeParameters(): 获取class的泛型类型
		 */
		GenericActualType genericActualType = new GenericActualType();
		genericActualType.printType();
		genericActualType.printParent();
		genericActualType.printSub();
		genericActualType.parameterizedTypeRef();
	}

	public void printType() {
		System.out.println("====> printType: ");
		List<? super String> list = new ArrayList<>();
		// 获取实际的泛型类型，但是只能获取到T，而不是具体类型
		TypeVariable<? extends Class<? extends List>>[] listTypeParameters = list.getClass().getTypeParameters();
		for (TypeVariable<? extends Class<? extends List>> listTypeParameter : listTypeParameters) {
			System.out.println(listTypeParameter.getName()); // 获取泛型名称：E
			System.out.println(listTypeParameter.getGenericDeclaration()); // 获取泛型声明的类：class java.util.ArrayList
			System.out.println(Arrays.toString(listTypeParameter.getBounds())); // 获取泛型边界定义：[class java.lang.Object]
			System.out.println(listTypeParameter.getTypeName()); // 获取具体泛型类型：E
		}
		System.out.println("getTypeParameters of ArrayList<String>: " + Arrays.toString(listTypeParameters));
		Type listGenericSuperclass = list.getClass().getGenericSuperclass();
		System.out.println("getGenericSuperclass of ArrayList<String>: " + listGenericSuperclass);
		System.out.println("class of getGenericSuperclass of ArrayList<String>: " + listGenericSuperclass.getClass());
	}

	public void printParent() {
		System.out.println("====> printParent: ");
		Parent<String> parent = new Parent<>();
		TypeVariable<? extends Class<? extends Parent>>[] parentTypeParameters = parent.getClass().getTypeParameters();
		Type genericSuperclass = parent.getClass().getGenericSuperclass();
		System.out.println(Arrays.toString(parentTypeParameters));
		System.out.println(genericSuperclass);
		System.out.println(parent);
		/*~:
		[T]
		class java.lang.Object
		 */
	}

	public void printSub() {
		System.out.println("====> printSub: ");
		Parent<Integer> sub = new Sub<>();
		TypeVariable<? extends Class<? extends Parent>>[] subTypeParameters = sub.getClass().getTypeParameters();
		Type genericSuperclass = sub.getClass().getGenericSuperclass();
		System.out.println(Arrays.toString(subTypeParameters));
		System.out.println(genericSuperclass);
		/*~:
		[T]
		com.belonk.lang.generic.Parent<T>
		 */
	}

	public void parameterizedTypeRef() {
		// 创建匿名子类，泛型通过父类指定
		ParameterizedTypeRef<String> parameterizedTypeRef = new ParameterizedTypeRef<String>() {
		};
		System.out.println(parameterizedTypeRef.getType()); // class java.lang.String
		ParameterizedTypeRef<Integer> intParameterizedTypeRef = new ParameterizedTypeRef<Integer>() {
		};
		System.out.println(intParameterizedTypeRef.getType());
		ParameterizedTypeRef<Parent<String>> parentParameterizedTypeRef = new ParameterizedTypeRef<Parent<String>>() {
		};
		System.out.println(parentParameterizedTypeRef.getType().getTypeName()); // com.belonk.lang.generic.Parent<java.lang.String>
		// 详细解析和使用可以查阅Spring的ResolvableType的源码
	}
}

class Parent<T> {

}

class Sub<T> extends Parent<T> {

}

// 用作获取泛型具体类型的引用对象，只能创建其子类示例
// 通过父类
abstract class ParameterizedTypeRef<T> {
	private Type type;

	public ParameterizedTypeRef() {
		Class<?> cls = findParameterizedTypeClass(this.getClass());
		Type genericSuperclass = cls.getGenericSuperclass();
		assert genericSuperclass instanceof ParameterizedType;
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		this.type = parameterizedType.getActualTypeArguments()[0];
	}

	public Type getType() {
		return type;
	}

	// 找到父类是ParameterizedTypeRef的类，从而根据父类可以获取其泛型类型
	private static Class<?> findParameterizedTypeClass(Class<?> child) {
		Class<?> parent = child.getSuperclass();
		if (parent == Object.class) // 没找到，抛异常
			throw new IllegalArgumentException();
		if (parent == ParameterizedTypeRef.class) // 找到，直接返回
			return child;
		return findParameterizedTypeClass(parent); // 递归查找
	}
}