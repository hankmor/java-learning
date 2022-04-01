package com.belonk.log.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sun on 2022/4/1.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class LoggerInstance {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		// 调用LoggerFactory.getLogger 具有相同名称的方法将始终返回对完全相同的记录器对象的引用。
		// 以记录器所在的类命名记录器似乎是迄今为止已知的最佳通用策略。
		Logger x = LoggerFactory.getLogger("wombat");
		Logger y = LoggerFactory.getLogger("wombat");
		System.out.println(x == y);
		// true
	}
}
