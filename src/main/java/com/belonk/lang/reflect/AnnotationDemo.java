package com.belonk.lang.reflect;

import java.lang.annotation.*;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * Created by sun on 2021/11/2.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class AnnotationDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		Class<SubDeclareAnnotation> aClass = SubDeclareAnnotation.class;
		System.out.println(Arrays.toString(aClass.getAnnotations())); // 获取自身和从弗雷继承的注解
		System.out.println(Arrays.toString(aClass.getDeclaredAnnotations())); // 获取自身标记的注解，不包括从父类获取

		AnnotatedType annotatedSuperclass = aClass.getAnnotatedSuperclass(); // 获取父类对应的反射注解类型，可以方便操作其注解
		// Class superclassType = (Class) annotatedSuperclass.getType();
		// Class<?> superclassType = aClass.getSuperclass();
		System.out.println(annotatedSuperclass.getType()); // 打印父类型：DeclareAnnotation
		System.out.println(Arrays.toString(annotatedSuperclass.getAnnotations())); // 打印父类的注解
		System.out.println(annotatedSuperclass.isAnnotationPresent(CustomAnnotation1.class)); // 检查是否标注了注解：true

		AnnotatedType[] annotatedInterfaces = aClass.getAnnotatedInterfaces(); // 获取aClass
		for (AnnotatedType annotatedInterface : annotatedInterfaces) {
			Type interfaceType = annotatedInterface.getType();
			System.out.println(interfaceType); // 获取接口类型：interface com.belonk.lang.reflect.AnnotatedInterface
		}
		/*
		[@com.belonk.lang.reflect.CustomAnnotation1(value=), @com.belonk.lang.reflect.CustomAnnotation2(value=)]
		[@com.belonk.lang.reflect.CustomAnnotation2(value=)]
		class com.belonk.lang.reflect.DeclareAnnotation
		[]
		false
		interface com.belonk.lang.reflect.AnnotatedInterface
		interface com.belonk.lang.reflect.Interface
		 */
	}
}

// 自定义注解

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Inherited
@interface CustomAnnotation0 {
	String value() default "";
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Inherited
@interface CustomAnnotation1 {
	String value() default "";
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Inherited
@interface CustomAnnotation2 {
	String value() default "";
}

// 接口
interface Interface {

}

// 接口标记注解
@CustomAnnotation0
interface AnnotatedInterface {

}

// 父类标记注解
@CustomAnnotation1
class DeclareAnnotation {

}

// 子类标记注解
@CustomAnnotation2
class SubDeclareAnnotation extends DeclareAnnotation implements AnnotatedInterface, Interface {

}