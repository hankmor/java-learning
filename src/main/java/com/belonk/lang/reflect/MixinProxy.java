package com.belonk.lang.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sun on 2021/11/7.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class MixinProxy implements InvocationHandler {
	//~ Static fields/constants/initializer

	// 方法和其对应的对象实例缓存，key为方法，value为实例对象
	private static final Map<String, Object> proxied = new ConcurrentHashMap<>();

	//~ Instance fields


	//~ Constructors

	public MixinProxy(TwoTuple... twoTuples) {
		for (TwoTuple twoTuple : twoTuples) {
			Method[] methods = twoTuple.getClazz().getMethods();
			// 找到接口声明的所有方法
			for (Method method : methods) {
				proxied.putIfAbsent(method.getName(), twoTuple.getObject());
			}
		}
	}

	//~ Methods

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 通过方法名称找到对象实例
		String name = method.getName();
		Object obj = proxied.get(name);
		// 调用方法
		return method.invoke(obj, args);
	}

	// 创建代理实例，实现传递的接口，接口不能重复，否则对应关系会错乱
	public static Object newInstance(TwoTuple... twoTuples) {
		Class<?>[] interfaces = new Class[twoTuples.length];
		int i = 0;
		// 遍历出需要实现的接口
		for (TwoTuple tuple : twoTuples) {
			interfaces[i++] = tuple.getClazz();
		}
		return Proxy.newProxyInstance(interfaces[0].getClassLoader(), interfaces, new MixinProxy(twoTuples));
	}

	public static class TwoTuple {
		private final Object object;
		private final Class<?> clazz;

		public TwoTuple(Object object, Class<?> bClass) {
			assert bClass.isInterface() && bClass.isInstance(object);
			this.object = object;
			this.clazz = bClass;
		}

		public Object getObject() {
			return object;
		}

		public Class<?> getClazz() {
			return clazz;
		}
	}
}