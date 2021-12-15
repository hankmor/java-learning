package com.belonk.jdk8.interfaces;

import java.util.Random;

/**
 * Created by sun on 2017/6/29.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class SimpleCalculator implements Calculator {
	//~ Static fields/initializers =====================================================================================

	private static Random random = new Random();
	//~ Instance fields ================================================================================================


	//~ Constructors ===================================================================================================


	//~ Methods ========================================================================================================

	@Override
	public double calc(int n) {
		return sqrt(n) * random.nextInt(100);
	}
}
