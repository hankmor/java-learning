package com.belonk.lang.reflect;

/**
 * Created by sun on 2021/11/3.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class TypeDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/**
	 * Type表示一个类型，是java反射中的通用超级接口，包括五个组件：
	 * 1、ParameterizedType: 描述泛型类型，如集合的泛型类型，比如List<String>，Map<String, Object>就是泛型类型。
	 * 2、TypeVariable: 类型变量，描述各种类型变量的超级接口，指的是ParameterizedType中的泛型变量，如定义泛型常见的T、E等，如
	 * List<String>整个表示ParameterizedType，但是其中的String就用TypeVariable表示。
	 * 3、WildcardType: 描述通配符的泛型类型, 比如：?、? extends Number、? super Integer这类通过通配符定义的泛型边界。
	 * 4、GenericArrayType: 描述泛型数组，表示组件类型为ParameterizedType或TypeVariable的数组。
	 * 5、Class: 描述类的字节码对象，每一个对象都有Class对象，可以通过Class反射地获取对象的字段、方法、构造器等。
	 */
	public static void main(String[] args) {
	}
}