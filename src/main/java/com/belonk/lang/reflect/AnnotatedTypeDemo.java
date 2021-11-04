package com.belonk.lang.reflect;

import java.io.Serializable;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sun on 2021/11/4.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class AnnotatedTypeDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/**
	 * Java自己编写的注解，可通过@Target指定标注的元素，包括以下可标注元素类型：
	 * 1、ElementType.TYPE：标注在类型上，包括接口、类、枚举、
	 * 2、ElementType.FIELD：标注在属性上，包括枚举的属性
	 * 3、ElementType.CONSTRUCTOR：标注在构造器上
	 * 4、ElementType.ANNOTATION_TYPE：
	 * 5、ElementType.METHOD:标注在方法上
	 * 6、ElementType.PARAMETER:标注在方法参数上
	 * 7、LOCAL_VARIABLE：标注本地变量上，即构造器、静态代码块、方法等的内部变量
	 * 8、PACKAGE：标注在包上
	 * 下边的两个常量在JDK8添加：
	 * 9、TYPE_PARAMETER：标注在类型参数上，比如泛型参数T、E上
	 * 10、TYPE_USE：使用类型，注解可以标注在任何类型变量上
	 * <p>
	 * AnnotatedType表示当前程序中可能被注解的类型，是用来描述被注解的类型的顶级接口，JDK8增加。这些类型是Java编程语言中的任何Type，
	 * 包括Class（原始类型）、 ParameterizedType（参数化的类型、泛型类型）、TypeVariable（类型变量）、WildcardType（通配符类型）、
	 * GenericArrayType（泛型数组类型）这五种java中的具体类型。
	 * <p>
	 * AnnotatedType继承了AnnotatedElement，获得获取注解的能力。
	 * <p>
	 * 它有几个子类型：
	 * 1、AnnotatedParameterizedType：带注解的参数化类型
	 * 2、AnnotatedTypeVariable：带注解的类型变量
	 * 3、AnnotatedArrayType：带注解的数组类型
	 * 4、AnnotatedWildcardType：带注解的通配符类型
	 * 5、AnnotatedTypeBaseImpl：带注解的基础类型，除了前边四个外
	 */
	public static void main(String[] args) {
		annotatedParameterizedType();
		annotatedTypeVariable();
		annotatedArrayType();
		annotatedWildcardType();
	}

	public static void annotatedParameterizedType() {
		System.out.println("==== annotatedParameterizedType: ");
		try {
			// getAnnotatedType：获取属性的AnnotatedType，这是一个AnnotatedParameterizedType
			AnnotatedType annotatedType = AnnotationClass.class.getDeclaredField("list").getAnnotatedType();
			System.out.println(annotatedType);
			assert annotatedType instanceof AnnotatedParameterizedType;
			// 拿到AnnotatedParameterizedType
			AnnotatedParameterizedType annotatedParameterizedType = (AnnotatedParameterizedType) annotatedType;
			// 获取实际类型参数，其实就是@MyAnno2("type-use") T部分
			AnnotatedType[] annotatedActualTypeArguments = annotatedParameterizedType.getAnnotatedActualTypeArguments();
			for (AnnotatedType annotatedActualTypeArgument : annotatedActualTypeArguments) {
				// 获取标注的注解信息
				Annotation[] declaredAnnotations = annotatedActualTypeArgument.getDeclaredAnnotations();
				System.out.println(Arrays.toString(declaredAnnotations));
			}
			/*
			sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedParameterizedTypeImpl@41629346
			[@com.belonk.lang.reflect.MyAnno2(value=[type-use])]
			 */
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public static void annotatedTypeVariable() {
		System.out.println("==== annotatedTypeVariable:");
		// AnnotationClass上定义的泛型：@MyAnno2("type-parameter-class") T extends @MyAnno2("type-use-class") Number & Serializable
		// TODO 不能直接获取Class的 AnnotatedTypeVariable？
		try {
			// 获取普通属性的注解，注意注解的Target必须申明Element.FIELD
			Field itemField = AnnotationClass.class.getDeclaredField("item");
			// 获取字段上定义的注解：@MyAnno2("field")
			Annotation[] declaredAnnotations = itemField.getDeclaredAnnotations();
			assert declaredAnnotations.length == 1;
			System.out.println(Arrays.toString(declaredAnnotations));

			Field field = AnnotationClass.class.getDeclaredField("type");
			AnnotatedType annotatedType = field.getAnnotatedType();
			assert annotatedType instanceof AnnotatedTypeVariable;
			AnnotatedTypeVariable annotatedTypeVariable = (AnnotatedTypeVariable) annotatedType;
			// 获取字段上定义的注解：@MyAnno2("field")
			declaredAnnotations = annotatedTypeVariable.getDeclaredAnnotations();
			System.out.println(Arrays.toString(declaredAnnotations));
			assert declaredAnnotations.length == 1;

			// 获取定义的泛型变量T的边界的注解类型
			// 变量T在Class定义：@MyAnno2("type-parameter") T extends @MyAnno2("type-use-class") Number & Serializable
			// 边界类为Number和Serializable，其中Number上标注了注解
			AnnotatedType[] annotatedBounds = annotatedTypeVariable.getAnnotatedBounds();
			for (AnnotatedType annotatedBound : annotatedBounds) {
				// 注解类型
				System.out.print(annotatedBound + " - ");
				// 打印边界的基础类型
				System.out.print(annotatedBound.getType() + " - ");
				// 查看定义的注解信息
				declaredAnnotations = annotatedBound.getDeclaredAnnotations();
				assert declaredAnnotations.length == 1;
				System.out.println(Arrays.toString(declaredAnnotations));
			}
			/*
		[@com.belonk.lang.reflect.MyAnno2(value=[item])]
		[@com.belonk.lang.reflect.MyAnno2(value=[field])]
		sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl@404b9385 - class java.lang.Number - [@com.belonk.lang.reflect.MyAnno2(value=[type-use-class])]
		sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl@6d311334 - interface java.io.Serializable - []
			 */
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public static void annotatedArrayType() {
		System.out.println("==== annotatedArrayType: ");
		try {
			// 拿到数组属性上的AnnotatedType
			AnnotatedType annotatedType = AnnotationClass.class.getDeclaredField("array").getAnnotatedType();
			assert annotatedType instanceof AnnotatedArrayType;
			AnnotatedArrayType annotatedArrayType = (AnnotatedArrayType) annotatedType;
			// 获取数组类型的潜在注释通用组件类型，数组使用类型变量T，该类型变量定义在Class上，故这里为AnnotatedTypeVariable类型
			AnnotatedTypeVariable annotatedTypeVariable = (AnnotatedTypeVariable) annotatedArrayType.getAnnotatedGenericComponentType();
			// 获取array属性上标注的注解信息
			Annotation[] declaredAnnotations = annotatedTypeVariable.getDeclaredAnnotations();
			assert declaredAnnotations.length == 1;
			System.out.println(Arrays.toString(declaredAnnotations));
			// 获取定义的泛型变量T的边界的注解类型
			// 变量T在Class定义：@MyAnno2("type-parameter") T extends @MyAnno2("type-use-class") Number & Serializable
			// 边界类为Number和Serializable，其中Number上标注了注解
			AnnotatedType[] annotatedBounds = annotatedTypeVariable.getAnnotatedBounds();
			for (AnnotatedType annotatedBound : annotatedBounds) {
				// 注解类型
				System.out.print(annotatedBound + " - ");
				// 打印边界的基础类型
				System.out.print(annotatedBound.getType() + " - ");
				// 查看定义的注解信息
				declaredAnnotations = annotatedBound.getDeclaredAnnotations();
				assert declaredAnnotations.length == 1;
				System.out.println(Arrays.toString(declaredAnnotations));
			}
			/*
			[@com.belonk.lang.reflect.MyAnno2(value=[type-use-array])]
			sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl@6d311334 - class java.lang.Number - [@com.belonk.lang.reflect.MyAnno2(value=[type-use-class])]
			sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl@682a0b20 - interface java.io.Serializable - []
			 */
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public static void annotatedWildcardType() {
		System.out.println("==== annotatedWildcardType: ");
		try {
			// 字段申明：List<@MyAnno2("type-use-wildcard") ? extends @MyAnno2("type-use-number") Number> list1
			AnnotatedType list1 = AnnotationClass.class.getDeclaredField("list1").getAnnotatedType();
			// 拿到参数化类型
			AnnotatedParameterizedType annotatedParameterizedType = (AnnotatedParameterizedType) list1;
			// 获取实际的AnnotatedType，申明了通配符，那么实际类型是AnnotatedWildcardType
			AnnotatedType[] annotatedActualTypeArguments = annotatedParameterizedType.getAnnotatedActualTypeArguments();
			for (AnnotatedType annotatedActualTypeArgument : annotatedActualTypeArguments) {
				System.out.println(annotatedActualTypeArgument);
				if (annotatedActualTypeArgument instanceof AnnotatedWildcardType) {
					AnnotatedWildcardType annotatedWildcardType = (AnnotatedWildcardType) annotatedActualTypeArgument;
					// 打印这个歌泛型类型参数的注解信息：MyAnno2(value=[type-use-wildcard])]
					Annotation[] declaredAnnotations = annotatedWildcardType.getDeclaredAnnotations();
					assert declaredAnnotations.length == 1;
					System.out.println(Arrays.toString(declaredAnnotations));
					// 获取下界的AnnotatedType，这里只定义了上界Number
					AnnotatedType[] annotatedLowerBounds = annotatedWildcardType.getAnnotatedLowerBounds();
					assert annotatedLowerBounds.length == 0;
					// 定义了上界Number，打印申明的注解信息：MyAnno2(value=[type-use-number])]
					AnnotatedType[] annotatedUpperBounds = annotatedWildcardType.getAnnotatedUpperBounds();
					assert annotatedUpperBounds.length == 1;
					for (AnnotatedType annotatedUpperBound : annotatedUpperBounds) {
						declaredAnnotations = annotatedUpperBound.getDeclaredAnnotations();
						assert declaredAnnotations.length == 1;
						System.out.println(Arrays.toString(declaredAnnotations));
					}
				}
			}
			/*
			sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedParameterizedTypeImpl@448139f0
			sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedWildcardTypeImpl@7cca494b
			[@com.belonk.lang.reflect.MyAnno2(value=[type-use-wildcard])]
			[@com.belonk.lang.reflect.MyAnno2(value=[type-use-number])]
			 */
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
}

// TYPE_USER：表示使用类型，可以标注在任何类型上，但是，可能影响元素获取注解的方式，例如：注解标注了TYPE_USER，但未标注FIELD，则通过
// Filed上的getXXXAnnotation方法不能获取到注解，只能通过AnnotatedType获取。
@Retention(RetentionPolicy.RUNTIME)
@Target({
		ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE/*, ElementType.TYPE_PARAMETER*/
		,
		ElementType.TYPE_USE
})
@Inherited
@interface MyAnno2 {
	String[] value() default "";
}

// TODO 怎么获取注解@MyAnno2("type-parameter-class")
class AnnotationClass<@MyAnno2("type-parameter-class") T extends @MyAnno2("type-use-class") Number & Serializable> {
	@MyAnno2("item")
	private Object item;

	@MyAnno2("field")
	private T type;

	private List<@MyAnno2("type-use") T> list;

	private @MyAnno2("type-use-array") T[] array;

	private List<@MyAnno2("type-use-wildcard") ? extends @MyAnno2("type-use-number") Number> list1;

	@MyAnno2("constructor")
	public AnnotationClass() {

	}

	public <@MyAnno2("type-parameter-method") I> void method(@MyAnno2("parameter") List<@MyAnno2("type-use-method") T> list, List<I> list1) {
		@MyAnno2("local-variable")
		int var = 1;
	}
}