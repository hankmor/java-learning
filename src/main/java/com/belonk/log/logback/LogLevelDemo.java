package com.belonk.log.logback;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;

/**
 * Created by sun on 2022/4/1.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class LogLevelDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {

		// get a logger instance named "com.foo". Let us further assume that the
		// logger is of type  ch.qos.logback.classic.Logger so that we can
		// set its level
		ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.foo");
		//set its Level to INFO. The setLevel() method requires a logback logger
		logger.setLevel(Level.INFO);

		// This request is enabled, because WARN >= INFO
		logger.warn("Low fuel level.");

		// This request is disabled, because DEBUG < INFO.
		logger.debug("Starting search for nearest gas station.");

		// 未设置级别，从父的appender com.foo 中继承级别INFO
		ch.qos.logback.classic.Logger barlogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("com.foo.Bar");
		// barlogger.setLevel(Level.DEBUG);

		// The logger instance barlogger, named "com.foo.Bar",
		// will inherit its level from the logger named
		// "com.foo" Thus, the following request is enabled
		// because INFO >= INFO.
		barlogger.info("Located nearest gas station.");

		// This request is disabled, because DEBUG < INFO.
		barlogger.debug("Exiting gas station search");
	}
}
