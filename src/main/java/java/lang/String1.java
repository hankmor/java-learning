package java.lang;

/**
 * Created by sun on 2020/8/8.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
// 测试用，该类会导致与String类冲突，故改名为String1，测试时应为String
public class String1 {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/**
	 * 自定义一个java.lang.String类，不能运行：在类 java.lang.String 中找不到 main 方法
	 * <p>
	 * 原因：类加载器的双亲委派机制，java.lang.String类交给了BootstrapClassloader加载的是rt.jar中的String，自定义的String无效，
	 * 双亲委派机制保住了安全性，避免用户自定义rt.jar包中的类而破坏Java程序。
	 */
	public static void main(String[] args) {
		System.out.println("Custom String Can't run.");
	}
}