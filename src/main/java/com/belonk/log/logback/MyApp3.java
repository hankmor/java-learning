package com.belonk.log.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sun on 2022/4/1.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class MyApp3 {
	//~ Static fields/constants/initializer

	private static final Logger logger = LoggerFactory.getLogger(MyApp3.class);

	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		// assume SLF4J is bound to logback in the current environment
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

		try {
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(context);
			// Call context.reset() to clear any previous configuration, e.g. default
			// configuration. For multi-step configuration, omit calling context.reset().
			context.reset();
			configurator.doConfigure(args[0]);
		} catch (Exception je) {
			// StatusPrinter will handle this
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(context);

		logger.info("Entering application.");

		Foo foo = new Foo();
		foo.doIt();
		logger.info("Exiting application.");
	}
}
