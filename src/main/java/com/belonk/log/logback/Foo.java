package com.belonk.log.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sun on 2022/4/1.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class Foo {
	//~ Static fields/constants/initializer

	static final Logger log = LoggerFactory.getLogger(Foo.class);

	//~ Instance fields


	//~ Constructors


	//~ Methods

	public void doIt() {
		log.debug("Did it again!");
	}

	public void doErr() {
		throw new RuntimeException("Some error");
	}
}
