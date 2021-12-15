package com.belonk.lang.reflect;

/**
 * Created by sun on 2021/11/21.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
@CustomAnno("class")
public class AnnotationPosition {
	//~ Static fields/constants/initializer

	@CustomAnno("field")
	private String str;

	//~ Instance fields


	//~ Constructors

	@CustomAnno("constructor")
	public AnnotationPosition() {
	}

	//~ Methods

	@CustomAnno("method")
	public void method(@CustomAnno("parameter") String param) {

	}

	public static void main(String[] args) {
		@CustomAnno("local_variable")
		int i = 0;
	}
}