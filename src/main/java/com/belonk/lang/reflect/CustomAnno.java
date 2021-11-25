package com.belonk.lang.reflect;

import java.lang.annotation.*;

/**
 * Created by sun on 2021/11/21.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
		ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR})
@Documented
public @interface CustomAnno {
	String value() default "";
}
