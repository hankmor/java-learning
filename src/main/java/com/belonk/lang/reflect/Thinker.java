package com.belonk.lang.reflect;

/**
 * Created by sun on 2021/11/7.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class Thinker implements Think {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	@Override
	public void think(String content) {
		System.out.println("thinker is thinking : " + content);
	}
}