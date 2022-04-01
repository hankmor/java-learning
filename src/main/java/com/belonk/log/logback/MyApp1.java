package com.belonk.log.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sun on 2022/4/1.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class MyApp1 {
	//~ Static fields/constants/initializer

	private static final Logger logger = LoggerFactory.getLogger(MyApp1.class);

	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		logger.info("Entering application.");

		// logback配置从classpath一次找logback-test.xml、logback.xml文件，如果没有找到，
		// 则再从META-INF\services\ch.qos.logback.classic.spi.Configurator
		// 通过spi加载配置，仍没有配置文件，执行BasicConfigurator类默认创建一个配置，默认的日志输出格式为：
		// %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
		Foo foo = new Foo();
		foo.doIt();
		logger.info("Exiting application.");
	}
}
