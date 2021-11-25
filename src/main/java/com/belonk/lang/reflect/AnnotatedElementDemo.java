package com.belonk.lang.reflect;

import com.belonk.util.Assert;

import java.lang.annotation.*;
import java.util.Arrays;

/**
 * AnnotationElement
 */
public class AnnotatedElementDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/**
	 * AnnotatedElement是用于获取元素上标注的注解的顶级接口，内部定义了多个获取注解的方法，Class类实现了该接口，故可以通过Class获取元
	 * 素上标注的注解。
	 * <p>
	 * 注解标注存在几种形式：直接存在、间接存在、存在、关联。
	 * 1、直接存在：注解直接标记在元素上，就说注解直接存在于该元素上
	 * 2、间接存在：一个被@Repeatable标注的注解标注在元素上，它就与该元素间接存在，因为底层class标注的不是它，而是@Repeatable指向的注解
	 * 3、存在：注解满足直接存在或者间接存在，或者类上的该注解可以从其父类继承得到，那么可以就说注解存在
	 * 4、关联：注解满足直接存在或间接存在，或者类的父类与该注解存在关联（直接存在或间接存在），就说该注解与类关联
	 * <p>
	 * 下表总结了在该接口中检查哪种注解存在不同的方法，不同 AnnotatedElement 方法检测到的存在种类概述：
	 * <p>
	 * 方法                                       直接存在    间接存在    存在    关联
	 * T getAnnotation(Class<T>)                                         X
	 * Annotation[] getAnnotations()                                     X
	 * T[] getAnnotationsByType(Class<T>)                                        X
	 * T getDeclaredAnnotation(Class<T>)              X
	 * Annotation[] getDeclaredAnnotations()          X
	 * T[] getDeclaredAnnotationsByType(Class<T>)     X           X
	 */
	public static void main(String[] args) {
		AnnotatedElementDemo demo = new AnnotatedElementDemo();
		demo.getAnnotation();
		demo.compositeAnnotation();
		demo.repeatableAnnotation();
		demo.annotationApi();
	}

	/**
	 * Class.getAnnotations()获取类的注解，包括从父类继承的；
	 * Class.getDeclaredAnnotations()获取自身被标注的注解，不包括继承的。
	 */
	public void getAnnotation() {
		System.out.println("==== getAnnotation: ");
		System.out.println(Arrays.toString(AnnotatedClass.class.getAnnotations()));
		System.out.println(Arrays.toString(SubAnnotatedClass.class.getAnnotations()));
		System.out.println(Arrays.toString(SubAnnotatedClass.class.getDeclaredAnnotations()));
		System.out.println(Arrays.toString(SubAnnotatedClass1.class.getAnnotations()));
		/*
		[@com.belonk.lang.reflect.MyAnno()]
		[@com.belonk.lang.reflect.MyAnno()]
		[]
		[@com.belonk.lang.reflect.MyAnno()]
		 */
	}

	public void compositeAnnotation() {
		System.out.println("==== compositeAnnotation: ");
		MyAnnos myAnnos = SubAnnotatedClass2.class.getAnnotation(MyAnnos.class);
		MyAnno[] value = myAnnos.value();
		System.out.println(Arrays.toString(value));

		/*
		[@com.belonk.lang.reflect.MyAnno(value=a), @com.belonk.lang.reflect.MyAnno(value=b)]
		 */
	}

	public void repeatableAnnotation() {
		System.out.println("==== repeatableAnnotation: ");
		System.out.println(Arrays.toString(AnnotatedClass1.class.getAnnotations()));
		// 子类继承注解
		System.out.println(Arrays.toString(AnnotatedClass2.class.getAnnotations()));
		/*
		[@com.belonk.lang.reflect.MyRepeatableAnno(value=[@com.belonk.lang.reflect.MyAnno1(value=a), @com.belonk.lang.reflect.MyAnno1(value=b)])]
		[@com.belonk.lang.reflect.MyRepeatableAnno(value=[@com.belonk.lang.reflect.MyAnno1(value=a), @com.belonk.lang.reflect.MyAnno1(value=b)])]
		 */
	}

	public void annotationApi() {
		System.out.println("==== annotationApi: ");

		/*
		getAnnotation于getAnnotationsByType的区别：前者不能获取可重复的注解，而是获取可重复注解下层的组合注解（@Repeatable的value指向的
		注解）；后者可以获取可重复注解和下层的组合注解。
		 */

		// 获取存在的注解，MyRepeatableAnno是间接存在于AnnotatedClass2上的，同样可以获取到
		MyRepeatableAnno myRepeatableAnno = AnnotatedClass2.class.getAnnotation(MyRepeatableAnno.class);
		Assert.notNull(myRepeatableAnno);
		// MyAnno1注解看似直接存在于AnnotatedClass1上，但他是可重复注解，实际上标注的是MyRepeatableAnno注解
		MyAnno1 myAnno1 = AnnotatedClass1.class.getAnnotation(MyAnno1.class);
		Assert.isNull(myAnno1);

		// 获取存在的注解，只能获取间接存在的MyRepeatableAnno注解
		Annotation[] annotations = AnnotatedClass2.class.getAnnotations();
		Assert.isTrue(annotations.length == 1);

		// 可以获取关联的注解，MyRepeatableAnno注解是从父类继承的
		MyRepeatableAnno[] annotationsByType = AnnotatedClass2.class.getAnnotationsByType(MyRepeatableAnno.class);
		Assert.isTrue(annotationsByType.length == 1);
		MyAnno1[] annotationsByType1 = AnnotatedClass2.class.getAnnotationsByType(MyAnno1.class);
		Assert.isTrue(annotationsByType1.length == 2);

		/*
		 * getDeclaredAnnotation(Class)与getDeclaredAnnotationsByType的区别：
		 * 1、两个都忽略继承的注解，都可获取指定类型的注解
		 * 2、getDeclaredAnnotation 获取直接存在的和间接存在的注解，不能获取重复注解
		 * 3、getDeclaredAnnotationsByType 获取直接存在和间接存在的注解，包括重复注解
		 */

		// 获取直接注解
		MyRepeatableAnno declaredAnnotation = AnnotatedClass2.class.getDeclaredAnnotation(MyRepeatableAnno.class);
		Assert.isNull(declaredAnnotation);
		// 获取直接存在的注解，该类上没有标注任何注解
		Annotation[] declaredAnnotations = AnnotatedClass2.class.getDeclaredAnnotations();
		Assert.isTrue(declaredAnnotations.length == 0);
		// 获取直接存在的注解，包括可重复注解的组合注解，不包括可重复注解
		Annotation[] declaredAnnotations1 = AnnotatedClass1.class.getDeclaredAnnotations();
		Assert.isTrue(declaredAnnotations1.length == 1);
		// 获取间接存在的注解
		MyRepeatableAnno declaredAnnotation2 = AnnotatedClass1.class.getDeclaredAnnotation(MyRepeatableAnno.class);
		Assert.notNull(declaredAnnotation2);
		// 不能获取重复注解
		MyAnno1 myAnno11 = AnnotatedClass1.class.getDeclaredAnnotation(MyAnno1.class);
		Assert.isNull(myAnno11);
		// 忽略继承
		MyRepeatableAnno[] declaredAnnotationsByType = AnnotatedClass2.class.getDeclaredAnnotationsByType(MyRepeatableAnno.class);
		Assert.isTrue(declaredAnnotationsByType.length == 0);
		// 获取直接存在的重复注解
		MyAnno1[] myAnno1s = AnnotatedClass3.class.getDeclaredAnnotationsByType(MyAnno1.class);
		Assert.isTrue(myAnno1s.length == 2);
		// 获取直接存在的非重复注解
		MyAnno[] myAnnos = AnnotatedClass3.class.getDeclaredAnnotationsByType(MyAnno.class);
		Assert.isTrue(myAnnos.length == 1);
		// 获取简介存在的注解
		MyRepeatableAnno[] myRepeatableAnnos = AnnotatedClass3.class.getDeclaredAnnotationsByType(MyRepeatableAnno.class);
		Assert.isTrue(myRepeatableAnnos.length == 1);

		// 检查是否存在指定注解，效果同getAnnotation(annotationClass) != null
		boolean annotationPresent = AnnotatedClass2.class.isAnnotationPresent(MyRepeatableAnno.class);
		Assert.isTrue(annotationPresent);
		boolean annotationPresent1 = AnnotatedClass2.class.isAnnotationPresent(MyAnno1.class);
		Assert.isTrue(!annotationPresent1);
	}
}

// 自定义注解，@Inherited表示标注该注解的父类，其子类可以继承得到该注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@interface MyAnno {
	String value() default "";
}

// 标注了注解的父类
@MyAnno
class AnnotatedClass {

}

// 注解继承
class SubAnnotatedClass extends AnnotatedClass {

}

// 子类
@MyAnno
// @MyAnno // 编译错误：duplicate annotation.
class SubAnnotatedClass1 extends AnnotatedClass {

}

// 组合注解，可以定义多个MyAnno注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface MyAnnos {
	MyAnno[] value() default {};
}

@MyAnnos({@MyAnno("a"), @MyAnno("b")})
class SubAnnotatedClass2 extends AnnotatedClass {

}

// ====== 可重复注解 ======

// Java的注解不允许重复标记，但是可以通过@Repeatable来定义可重复的注解，其实只是语法糖，利用注解持有其他注解数组的方式来实现，实际的
// class文件仍然不允许重复标记。
//
// 再定义一个注解，它被标记了@Repeatable注解，表示该注解可以重复标记
// 重复标记时，@Repeatable注释一个语法糖，实际上类上标注的是MyRepeatableAnno注解：
//  @MyRepeatableAnno({@MyAnno1("a"), @MyAnno1("b")})
//
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Repeatable(MyRepeatableAnno.class)
@interface MyAnno1 {
	String value() default "";
}

// 可重复注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@interface MyRepeatableAnno {
	MyAnno1[] value() default {};
}

@MyAnno1("a")
@MyAnno1("b")
class AnnotatedClass1 {

}

class AnnotatedClass2 extends AnnotatedClass1 {

}

@MyAnno1("a")
@MyAnno1("b")
@MyAnno
class AnnotatedClass3 {

}