package com.belonk.log.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sun on 2022/4/1.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class MyApp2 {
	//~ Static fields/constants/initializer

	private static final Logger log = LoggerFactory.getLogger(MyApp2.class);

	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		// assume SLF4J is bound to logback in the current environment
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		// print logback's internal status
		StatusPrinter.print(lc);

		log.info("Entering application.");
		Foo foo = new Foo();
		foo.doIt();
		foo.doErr();
		log.info("Exiting application.");
	}
}
