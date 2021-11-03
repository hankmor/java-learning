package com.belonk.lang.reflect;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

/**
 * TypeVariable：所有的泛型类型声明中的具体类型，都可以用TypeVariable来描述，比如T、E泛型符号，或者泛型具体的类型
 * 包括几个方法：
 * 1、getBounds：获取泛型类型变量的所有上边界，泛型声明只能用extends关键字，而super只能用来声明泛型通配符，所以只能获取上边界，而没有下边界。
 * 2、getGenericDeclaration：获取定义类型变量的对象，包括Constructor、Class、Method。
 * 3、getName：类型变量的名称，例如T、E。
 * 4、getAnnotatedBounds：返回一个 AnnotatedType 对象数组，这些对象表示使用类型来表示由此 TypeVariable 表示的类型参数的上限。 数组
 * 中对象的顺序对应于类型参数声明中边界的顺序。如果类型参数声明没有边界，则返回长度为 0 的数组。
 * <p>
 * Class的getTypeParameters()方法可以获取类上的泛型申明中的类型变量
 * <p>
 * Created by sun on 2021/11/3.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class TypeVariableDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields

	// 泛型属性
	private List<Long> list;

	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		TypeVariableDemo typeVariableDemo = new TypeVariableDemo();
		typeVariableDemo.testGenericField();
		typeVariableDemo.testGenericClass();
		typeVariableDemo.testGenericConstructor();
		typeVariableDemo.testGenericMethod();
	}

	public void testGenericField() {
		System.out.println("==== 泛型属性类型变量：");
		try {
			// 泛型属性使用 ParameterizedType 描述泛型
			Field list = TypeVariableDemo.class.getDeclaredField("list");
			Type genericType = list.getGenericType();
			assert genericType instanceof ParameterizedType;
			ParameterizedType parameterizedType = (ParameterizedType) genericType;
			Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
			for (Type actualTypeArgument : actualTypeArguments) {
				System.out.println(actualTypeArgument.getTypeName());
			}
			/*
			 java.lang.Long
			 */
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	// 定义泛型类，定义了上边界Number和Serializable接口
	class TypeVariableTestClass<T extends Number & Serializable> {
		// public TypeVariableTestClass() {
		// }

		// 泛型构造器
		public <I extends Number & Serializable> TypeVariableTestClass(I item) {
		}

		// public <I> void method(I item) {
		// }

		public <I extends Number & Serializable> void method(I item) {
		}
	}

	public void testGenericClass() {
		System.out.println("==== 类泛型声明的泛型变量：");
		// 获取类上的类型变量
		TypeVariable<Class<TypeVariableTestClass>>[] typeVariables = TypeVariableTestClass.class.getTypeParameters();
		for (TypeVariable<Class<TypeVariableTestClass>> typeVariable : typeVariables) {
			System.out.println(typeVariable);
			// 泛型上边界
			System.out.println(Arrays.toString(typeVariable.getBounds()));
			// 申明泛型的类
			System.out.println(typeVariable.getGenericDeclaration());
			// 类型变量的名称
			System.out.println(typeVariable.getName());
			// 注解边界
			System.out.println(Arrays.toString(typeVariable.getAnnotatedBounds()));
		}
		/*
		T
		[class java.lang.Number, interface java.io.Serializable]
		class com.belonk.lang.reflect.TypeVariableDemo$TypeVariableTestClass
		T
		[sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl@548c4f57, sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl@1218025c]
		 */
	}

	public void testGenericConstructor() {
		System.out.println("==== 泛型构造器类型变量：");
		// 获取到第二个构造器
		Constructor<?> declaredConstructor = TypeVariableTestClass.class.getConstructors()[0];
		// 获取构造器类型变量
		TypeVariable<? extends Constructor<?>>[] typeParameters = declaredConstructor.getTypeParameters();
		for (TypeVariable<? extends Constructor<?>> typeVariable : typeParameters) {
			// 泛型上边界
			System.out.println(Arrays.toString(typeVariable.getBounds()));
			// 申明泛型的类
			System.out.println(typeVariable.getGenericDeclaration());
			// 类型变量的名称
			System.out.println(typeVariable.getName());
			// 注解边界
			System.out.println(Arrays.toString(typeVariable.getAnnotatedBounds()));
		}
		/*
		[class java.lang.Number, interface java.io.Serializable]
		public com.belonk.lang.reflect.TypeVariableDemo$TypeVariableTestClass(com.belonk.lang.reflect.TypeVariableDemo,java.lang.Number)
		I
		[sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl@548c4f57, sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl@1218025c]
		 */
	}

	public void testGenericMethod() {
		System.out.println("==== 泛型方法类型变量：");
		// 获取泛型方法
		Method method = TypeVariableTestClass.class.getMethods()[0];
		TypeVariable<Method>[] typeParameters = method.getTypeParameters();
		for (TypeVariable<Method> typeVariable : typeParameters) {
			// 泛型上边界
			System.out.println(Arrays.toString(typeVariable.getBounds()));
			// 申明泛型的类
			System.out.println(typeVariable.getGenericDeclaration());
			// 类型变量的名称
			System.out.println(typeVariable.getName());
			// 注解边界
			System.out.println(Arrays.toString(typeVariable.getAnnotatedBounds()));
		}
		/*
		[class java.lang.Number, interface java.io.Serializable]
		public void com.belonk.lang.reflect.TypeVariableDemo$TypeVariableTestClass.method(java.lang.Number)
		I
		[sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl@3e3abc88, sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl@6ce253f1]
		 */
	}
}