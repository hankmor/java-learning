package com.belonk.lang.reflect;

/**
 * Created by sun on 2021/11/7.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class Person implements Talk {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	@Override
	public void talk(String content) {
		System.out.println("person is talking : " + content);
	}
}