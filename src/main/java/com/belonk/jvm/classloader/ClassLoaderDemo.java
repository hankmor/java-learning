package com.belonk.jvm.classloader;

/**
 * Created by sun on 2020/8/8.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ClassLoaderDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/**
	 * 顶层的BootstrapClassloader打印出null.
	 * <p>
	 * 类加载器：
	 * 1、bootstrap类加载器：加载JAVA_HOME/jre/lib/rt.jar
	 * 2、ExtClassloader：加载JAVA_HOME/jre/ext目录下的jar，用于扩展类的加载
	 * 3、AppClassLoader：加载CLASSPATH中的jar
	 * 4、自定义类加载器：继承Java.lang.ClassLoader
	 * 加载类时从下到上进行委派，顶层类加载不到再从上到下顺序加载，称为双亲委派机制。
	 * <p>
	 * 双亲委派机制：委托机制，子类加载器持有其父类加载引用，自身先不加载类，而是委托给父类加载器加载，如果父类加载不能加载，
	 * 则继续向上委托给父类加载，知道bootstrap class loader为止。如果直到bootstrap class loader仍然不能加载类，则从bootstrap
	 * 类加载器开始，交给子类加载器加载，直到类加载成功。
	 */
	public static void main(String[] args) {
		// java.lang.String的类加载
		ClassLoader classLoader = String.class.getClassLoader();
		// 输出null
		System.out.println(classLoader);

		ClassLoader classLoader1 = ClassLoaderDemo.class.getClassLoader();
		// sun.misc.Launcher$AppClassLoader@18b4aac2
		System.out.println(classLoader1);
		// sun.misc.Launcher$ExtClassLoader@3f99bd52
		System.out.println(classLoader1.getParent());
		// null
		System.out.println(classLoader1.getParent().getParent());

		// 数组的classloader，使用的是其元素的classloader
		Integer[] integers = {1, 2, 3};
		// 基础类型、String，classloader为null
		System.out.println(integers.getClass().getClassLoader());

		Cls[] cls = {new Cls(), new Cls()};
		// sun.misc.Launcher$AppClassLoader@18b4aac2
		System.out.println(cls.getClass().getClassLoader());
	}
}

class Cls {

}