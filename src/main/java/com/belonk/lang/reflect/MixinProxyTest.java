package com.belonk.lang.reflect;

/**
 * Created by sun on 2021/11/7.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class MixinProxyTest {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		// 创建混型代理对象，实现了Talk、Run、Think三个接口
		Object mixin = MixinProxy.newInstance(
				new MixinProxy.TwoTuple(new Person(), Talk.class),
				new MixinProxy.TwoTuple(new Tiger(), Run.class),
				new MixinProxy.TwoTuple(new Thinker(), Think.class)
		);
		Talk person = (Talk) mixin;
		person.talk("i am a person.");
		Run tiger = (Run) mixin;
		tiger.run(10000);
		Think thinker = (Think) mixin;
		thinker.think("i am always thinking those useless things.");

		/*
		person is talking : i am a person.
		tiger ran 10000 meters.
		thinker is thinking : i am always thinking those useless things.
		 */
	}
}