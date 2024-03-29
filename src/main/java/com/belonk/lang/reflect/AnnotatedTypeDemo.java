package com.belonk.lang.reflect;

import com.belonk.util.Assert;

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
		annotatedMethodType();
		annotatedLocalVariable();
	}

	public static void annotatedParameterizedType() {
		System.out.println("==== annotatedParameterizedType: ");
		try {
			// getAnnotatedType：获取属性的AnnotatedType，这是一个AnnotatedParameterizedType
			AnnotatedType annotatedType = AnnotationClass.class.getDeclaredField("list").getAnnotatedType();
			System.out.println(annotatedType);
			Assert.isTrue(annotatedType instanceof AnnotatedParameterizedType);
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
		// 不能直接获取Class的 AnnotatedTypeVariable，只能通过泛型子类获取
		try {
			// 获取泛型子类的泛型父类上泛型的注解
			AnnotatedType annotatedType1 = AnnotationSubClass1.class.getAnnotatedSuperclass();
			Assert.isTrue(annotatedType1 instanceof AnnotatedParameterizedType);
			AnnotatedParameterizedType apt = (AnnotatedParameterizedType) annotatedType1;
			AnnotatedType[] annotatedActualTypeArguments = apt.getAnnotatedActualTypeArguments();
			// 只有一个泛型参数
			AnnotatedTypeVariable annotatedTypeVariable1 = (AnnotatedTypeVariable) annotatedActualTypeArguments[0];
			Annotation[] annotations = annotatedTypeVariable1.getAnnotations();
			System.out.println(Arrays.toString(annotations));

			// 获取非泛型子类的泛型父类的注解
			AnnotatedType annotatedType2 = AnnotationSubClass2.class.getAnnotatedSuperclass();
			Assert.isTrue(annotatedType2 instanceof AnnotatedParameterizedType);
			AnnotatedParameterizedType apt2 = (AnnotatedParameterizedType) annotatedType2;
			AnnotatedType[] annotatedActualTypeArguments1 = apt2.getAnnotatedActualTypeArguments();
			AnnotatedType annotatedType3 = annotatedActualTypeArguments1[0];
			System.out.println(Arrays.toString(annotatedType3.getAnnotations()));

			// 获取普通属性的注解，注意注解的Target必须申明Element.FIELD
			Field itemField = AnnotationClass.class.getDeclaredField("item");
			// 获取字段上定义的注解：@MyAnno2("field")
			Annotation[] declaredAnnotations = itemField.getDeclaredAnnotations();
			Assert.isTrue(declaredAnnotations.length == 1);
			System.out.println(Arrays.toString(declaredAnnotations));

			// 根据域的泛型类型，获取该泛型类型（定义在类上）的注解
			Field field = AnnotationClass.class.getDeclaredField("type");
			AnnotatedType annotatedType = field.getAnnotatedType();
			// 是一个AnnotatedTypeVariable类型，表示被注解标注的类型变量
			Assert.isTrue(annotatedType instanceof AnnotatedTypeVariable);
			AnnotatedTypeVariable annotatedTypeVariable = (AnnotatedTypeVariable) annotatedType;
			// 获取字段上定义的注解：@MyAnno2("field")
			declaredAnnotations = annotatedTypeVariable.getDeclaredAnnotations();
			Assert.isTrue(declaredAnnotations.length == 1);
			System.out.println(Arrays.toString(declaredAnnotations));
			// 获取类型变量 T 上的注解，getType获取到的是一个TypeVariable，通过它可以获取注解
			TypeVariable<?> typeVariable = (TypeVariable<?>) annotatedTypeVariable.getType();
			System.out.println(Arrays.toString(typeVariable.getAnnotations()));

			// 获取定义的泛型变量T的边界的注解类型
			// 变量T在Class定义：@MyAnno2("type-parameter") T extends @MyAnno2("type-use-class") Number & Serializable
			// 边界类为Number和Serializable
			AnnotatedType[] annotatedBounds = annotatedTypeVariable.getAnnotatedBounds();
			for (AnnotatedType annotatedBound : annotatedBounds) {
				// 注解类型
				System.out.print(annotatedBound + " - ");
				// 打印边界的基础类型
				System.out.print(annotatedBound.getType() + " - ");
				// 查看定义的注解信息
				declaredAnnotations = annotatedBound.getDeclaredAnnotations();
				Assert.isTrue(declaredAnnotations.length == 1);
				System.out.println(Arrays.toString(declaredAnnotations));
			}
			/*
			[@com.belonk.lang.reflect.MyAnno2(value=[super-class-1])]
			[@com.belonk.lang.reflect.MyAnno2(value=[super-class-2])]
			[@com.belonk.lang.reflect.MyAnno2(value=[item])]
			[@com.belonk.lang.reflect.MyAnno2(value=[field])]
			[@com.belonk.lang.reflect.MyAnno2(value=[type-parameter-class])]
			sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl@816f27d - class java.lang.Number - [@com.belonk.lang.reflect.MyAnno2(value=[type-use-class-number])]
			sun.reflect.annotation.AnnotatedTypeFactory$AnnotatedTypeBaseImpl@87aac27 - interface java.io.Serializable - [@com.belonk.lang.reflect.MyAnno2(value=[type-use-serializable])]
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
			Assert.isTrue(annotatedType instanceof AnnotatedArrayType);
			AnnotatedArrayType annotatedArrayType = (AnnotatedArrayType) annotatedType;
			// 获取数组类型的潜在注释通用组件类型，数组使用类型变量T，该类型变量定义在Class上，故这里为AnnotatedTypeVariable类型
			AnnotatedTypeVariable annotatedTypeVariable = (AnnotatedTypeVariable) annotatedArrayType.getAnnotatedGenericComponentType();
			// 获取array属性上标注的注解信息
			Annotation[] declaredAnnotations = annotatedTypeVariable.getDeclaredAnnotations();
			Assert.isTrue(declaredAnnotations.length == 1);
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
				Assert.isTrue(declaredAnnotations.length == 1);
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
			AnnotatedWildcardType annotatedWildcardType = (AnnotatedWildcardType) annotatedActualTypeArguments[0];
			// 打印这个歌泛型类型参数的注解信息：MyAnno2(value=[type-use-wildcard])]
			Annotation[] declaredAnnotations = annotatedWildcardType.getDeclaredAnnotations();
			Assert.isTrue(declaredAnnotations.length == 1);
			System.out.println(Arrays.toString(declaredAnnotations));
			// 获取下界的AnnotatedType，这里只定义了上界Number
			AnnotatedType[] annotatedLowerBounds = annotatedWildcardType.getAnnotatedLowerBounds();
			Assert.isTrue(annotatedLowerBounds.length == 0);
			// 定义了上界Number，打印申明的注解信息：MyAnno2(value=[type-use-number])]
			AnnotatedType[] annotatedUpperBounds = annotatedWildcardType.getAnnotatedUpperBounds();
			Assert.isTrue(annotatedUpperBounds.length == 1);
			for (AnnotatedType annotatedUpperBound : annotatedUpperBounds) {
				declaredAnnotations = annotatedUpperBound.getDeclaredAnnotations();
				Assert.isTrue(declaredAnnotations.length == 1);
				System.out.println(Arrays.toString(declaredAnnotations));
			}
			/*
			[@com.belonk.lang.reflect.MyAnno2(value=[type-use-wildcard])]
			[@com.belonk.lang.reflect.MyAnno2(value=[type-use-number])]
			 */
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	private static void annotatedMethodType() {
		System.out.println("==== annotatedMethodType: ");

		try {
			// 获取返回类型，这里为void
			Method method = AnnotationClass.class.getMethod("method", List.class, List.class);
			AnnotatedType annotatedReturnType = method.getAnnotatedReturnType();
			System.out.println(annotatedReturnType.getType()); // void

			// 获取方法泛型类型上的注解
			TypeVariable<Method>[] typeParameters = method.getTypeParameters();
			for (TypeVariable<Method> typeParameter : typeParameters) {
				System.out.println(Arrays.toString(typeParameter.getAnnotations()));
			}

			// 获取参数注解
			AnnotatedType[] annotatedParameterTypes = method.getAnnotatedParameterTypes();
			for (AnnotatedType annotatedParameterType : annotatedParameterTypes) {
				AnnotatedParameterizedType annotatedParameterizedType = (AnnotatedParameterizedType) annotatedParameterType;
				System.out.println(Arrays.toString(annotatedParameterizedType.getAnnotations()));
				AnnotatedType[] annotatedActualTypeArguments = annotatedParameterizedType.getAnnotatedActualTypeArguments();
				for (AnnotatedType annotatedActualTypeArgument : annotatedActualTypeArguments) {
					AnnotatedTypeVariable annotatedTypeVariable = (AnnotatedTypeVariable) annotatedActualTypeArgument;
					System.out.println(Arrays.toString(annotatedTypeVariable.getAnnotations()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		void
		[@com.belonk.lang.reflect.MyAnno2(value=[type-parameter-method])]
		[@com.belonk.lang.reflect.MyAnno2(value=[parameter])]
		[@com.belonk.lang.reflect.MyAnno2(value=[type-use-method])]
		[]
		[]
		 */
	}

	public static void annotatedLocalVariable() {
		// TODO JDK1.8目前的ElementType.LOCAL_VARIABLE暂时没有用到，未找到反射获取方式
	}
}

// TYPE_USE：表示使用类型，可以标注在任何类型上，但是，可能影响元素获取注解的方式，例如：注解标注了TYPE_USE，但未标注FIELD，则通过
// Filed上的getXXXAnnotation方法不能获取到注解，只能通过AnnotatedType获取。
@Retention(RetentionPolicy.RUNTIME)
@Target({
		ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE
		//, ElementType.TYPE_PARAMETER
		, ElementType.TYPE_USE
})
@Inherited
@interface MyAnno2 {
	String[] value() default "";
}

class AnnotationClass<@MyAnno2("type-parameter-class") T extends @MyAnno2("type-use-class-number") Number & @MyAnno2("type-use-serializable") Serializable> {
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
		int i = 1;
	}
}

class AnnotationSubClass1<T extends Number> extends AnnotationClass<@MyAnno2("super-class-1") T> {

}

class AnnotationSubClass2 extends AnnotationClass<@MyAnno2("super-class-2") Long> {

}

// ==== 测试注解可标记的TYPE_PARAMETER位置

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_PARAMETER})
@Inherited
@interface TypeParamAnno {
	String[] value() default "";
}

// '@TypeParamAnno' not applicable to type
/*@TypeParamAnno*/
// '@TypeParamAnno' not applicable to type use
class NumberHolder<@TypeParamAnno T extends /*@TypeParamAnno*/ Number> {
	// @TypeParamAnno' not applicable to field
	/*@TypeParamAnno*/ T number;
	// @TypeParamAnno' not applicable to type use
	List</*@TypeParamAnno*/ ? extends /*@TypeParamAnno*/ Number> numbers;

	// '@TypeParamAnno' not applicable to parameter
	// '@TypeParamAnno' not applicable to method
	public /*@TypeParamAnno*/ T add(/*@TypeParamAnno*/ T number) {
		return null;
	}

	// '@TypeParamAnno' not applicable to parameter
	public <@TypeParamAnno E> void m1(/*@TypeParamAnno*/ E e) {
	}

	// '@TypeParamAnno' not applicable to parameter
	public <@TypeParamAnno E extends Number> void m2(/*@TypeParamAnno*/ E e) {

	}

	// '@TypeParamAnno' not applicable to type use
	public void m2(List</*@TypeParamAnno*/ ? extends Number> e) {

	}
}

// ==== 测试注解可标记的TYPE_USE位置

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_USE})
@Inherited
@interface TypeAnno {
	String[] value() default "";
}

@TypeAnno
class NumberHolder1<@TypeAnno T extends @TypeAnno Number> {
	@TypeAnno T number;
	List<@TypeAnno ? extends @TypeAnno Number> numbers;

	public @TypeAnno T add(@TypeAnno T number) {
		return null;
	}

	public <@TypeAnno E> void m1(@TypeAnno E e) {
	}

	public <@TypeAnno E extends Number> void m2(@TypeAnno E e) {

	}

	public void m2(List<@TypeAnno ? extends Number> e) {

	}
}