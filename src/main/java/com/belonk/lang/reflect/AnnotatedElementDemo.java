package com.belonk.lang.reflect;

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
	 * 注解标注存在几种形式：直接存在、间接存在、存在、关联。
	 * 1、直接存在：注解直接标记在元素上，就说注解直接存在于该元素上
	 * 2、间接存在：一个被@Repeatable标注的注解标注在元素上，它就与该元素间接存在，因为底层class标注的不是它，而是@Repeatable指向的注解
	 * 3、存在：注解满足直接存在或者间接存在，或者类上的该注解可以从其父类继承得到，那么可以就说注解存在
	 * 4、关联：注解满足直接存在或间接存在，或者类的父类与该注解存在关联（直接存在或间接存在），就说该注解与类关联
	 *
	 下表总结了在该接口中检查哪种注解存在不同的方法，不同 AnnotatedElement 方法检测到的存在种类概述：

	 方法                                       直接存在    间接存在    存在    关联
	 T getAnnotation(Class<T>)                                         X
	 Annotation[] getAnnotations()                                     X
	 T[] getAnnotationsByType(Class<T>)                                        X
	 T getDeclaredAnnotation(Class<T>)              X
	 Annotation[] getDeclaredAnnotations()          X
	 T[] getDeclaredAnnotationsByType(Class<T>)     X           X
	 *
	 */
	public static void main(String[] args) {
		AnnotatedElementDemo demo = new AnnotatedElementDemo();
		demo.getAnnotation();
		demo.repeatableAnnotation();
		demo.annotationReflect();
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
		/*
		[@com.belonk.lang.reflect.MyAnno()]
		[@com.belonk.lang.reflect.MyAnno()]
		[]
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

	public void annotationReflect() {
		// TODO
		Annotation[] annotations = AnnotatedClass1.class.getAnnotations();
	}
}

// 自定义注解，@Inherited表示标注该注解的父类，其子类可以继承得到该注解
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@interface MyAnno {
}

// 标注了注解的父类
@MyAnno
class AnnotatedClass {

}

// 子类
@MyAnno
// @MyAnno // 编译错误：duplicate annotation.
class SubAnnotatedClass extends AnnotatedClass {

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