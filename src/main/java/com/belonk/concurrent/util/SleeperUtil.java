package com.belonk.concurrent.util;

import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/4/13.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public final class SleeperUtil {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void seconds(long seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void milliseconds(long milliseconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}