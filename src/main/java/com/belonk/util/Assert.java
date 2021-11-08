package com.belonk.util;

/**
 * Created by sun on 2021/11/8.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public final class Assert {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void isTrue(boolean bool) {
		if (!bool)
			throw new RuntimeException("assert failed, except TRUE, but get FALSE.");
	}

	public static void notNull(Object obj) {
		if (obj == null)
			throw new NullPointerException("assert failed, except non-null object.");
	}
}