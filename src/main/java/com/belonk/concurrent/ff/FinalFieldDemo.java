package com.belonk.concurrent.ff;

/**
 * Created by sun on 2021/4/7.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class FinalFieldDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		/*
		 * Final域重排序规则：
		 * 1、构造函数中赋值(写)final域，不会将写操作重排序到构造函数之外。编译器在写final之后，构造函数return之前插入StoreStore屏障来禁止重排序
		 * 2、读final域时，编译器禁止在读对象引用和读对象中的final域进行重排序（读final域前面添加LoadLoad屏障）。确保读final域之前先读对象引用
		 */
		for (int i = 0; i < 1000000; i++) {
			new Thread(FinalCls::write).start();
			new Thread(FinalCls::read).start();
		}
	}
}

class FinalCls {
	int i;
	final int j; // final域，在构造函数赋值
	static FinalCls obj;

	FinalCls() { // 构造函数
		i = 1; // 写普通域
		j = 2; // 写final域
	}

	public void set() {
		// j = 2; // 编译错误，不能给final变量赋值
		final int a;
		a = 11;  // 方法作用域，先声明后赋值
	}

	public static void write() {
		obj = new FinalCls(); // 写对象引用
		if (obj.i == 0) {
			System.err.println("写普通域出错");
		}
		if (obj.j == 0) {
			System.out.println("写final域出错");
		}
	}

	public static void read() {
		FinalCls obj1 = obj; // 读对象引用
		if (obj1 != null) {
			int x = obj1.i; // 读普通域
			int y = obj1.j; // 读final域
			if (x == 0) {
				System.err.println("读普通域出错");
			}
			if (y == 0) {
				System.out.println("读final与出错");
			}
		}
	}
}