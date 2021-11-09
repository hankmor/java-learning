package com.belonk.lang.reflect;

import com.belonk.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;

/**
 * Created by sun on 2021/11/8.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class UserReflectDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		UserReflectDemo demo = new UserReflectDemo();
		User user = demo.create();
		Assert.isTrue(user.equals(User.NULL));
		// 通过==判断getClass与class
		Assert.isTrue(user.getClass() == User.class);
		// 反射方法
		demo.setNameAndAge(user, "sam", 18);
		System.out.println(user);
		demo.isInstance(user);
		demo.getFields();
		demo.fieldApi(user);
		demo.constructorApi();
		demo.getPublicMethods();
		demo.getAllMethods();
		demo.methodApi(user);
		demo.parameterApi();
		demo.modifierApi();
	}

	public User create() {
		try {
			// 获取类的字节码对象
			Class<?> userClass = Class.forName("com.belonk.lang.reflect.User");
			return (User) userClass.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setNameAndAge(User user, String name, int age) {
		try {
			Method setName = User.class.getMethod("setName", String.class);
			setName.invoke(user, name);
			Method setAge = User.class.getMethod("setAge", int.class);
			setAge.invoke(user, age);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void isInstance(User user) {
		System.out.println("==== isInstance: ");
		System.out.println(user instanceof Id);
		System.out.println(User.class.isInstance(user));
		System.out.println(Id.class.isInstance(user));
	}

	public void getFields() {
		System.out.println("==== getFields: ");

		System.out.println("public fields:");
		Field[] fields = User.class.getFields();
		System.out.println(Arrays.toString(fields));

		System.out.println("field NULL:");
		try {
			Field aNull = User.class.getField("NULL");
			System.out.println(aNull);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		System.out.println("field NAME:");
		try {
			Field name = User.class.getDeclaredField("name");
			System.out.println(name);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("all fields:");
		Field[] declaredFields = User.class.getDeclaredFields();
		System.out.println(Arrays.toString(declaredFields));
	}

	public void fieldApi(User user) {
		System.out.println("==== fieldApi: ");
		try {
			// 包访问域
			Field lat = User.class.getDeclaredField("lat");
			lat.setDouble(user, 92.123456d);
			double latVal = lat.getDouble(user);
			System.out.println(latVal);

			// protected域
			Field salary = User.class.getDeclaredField("salary");
			salary.setFloat(user, 1000.99f);
			float salaryVal = salary.getFloat(user);
			System.out.println(salaryVal);

			// public static 域
			Field aNull = User.class.getField("NULL");
			User emtpyUser = (User) aNull.get(user);
			System.out.println(emtpyUser);

			// private域
			Field name = User.class.getDeclaredField("name");
			// private域需要设置访问权限
			name.setAccessible(true);
			String nameVal = (String) name.get(user);
			System.out.println(nameVal);

			Field age = User.class.getDeclaredField("age");
			age.setAccessible(true);
			int ageInt = age.getInt(user);
			System.out.println(ageInt);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void constructorApi() {
		System.out.println("==== constructorApi: ");
		// 获取所有公共构造函数
		Constructor<?>[] publicConstructors = User.class.getConstructors();
		System.out.println(Arrays.toString(publicConstructors));

		// 获取所有构造函数
		Constructor<?>[] allConstructors = User.class.getDeclaredConstructors();
		System.out.println(Arrays.toString(allConstructors));

		try {
			// 获取private的构造函数
			Constructor<User> nameConstructor = User.class.getDeclaredConstructor(String.class);
			// private构造函数，需要设置可以访问
			nameConstructor.setAccessible(true);
			User sam = nameConstructor.newInstance("sam");
			System.out.println(sam);

			// 打印申明类
			System.out.println(nameConstructor.getDeclaringClass());
		} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void getPublicMethods() {
		System.out.println("==== getPublicMethods: ");
		Method[] methods = User.class.getMethods();
		Arrays.stream(methods).forEach(System.out::println);
	}

	public void getAllMethods() {
		System.out.println("==== getAllMethods: ");
		Method[] declaredMethods = User.class.getDeclaredMethods();
		Arrays.stream(declaredMethods).forEach(System.out::println);
	}

	public void methodApi(User user) {
		try {
			System.out.println("==== methodApi: ");
			// 获取public的talk方法
			Method talk = User.class.getMethod("talk", String.class);
			Object result = talk.invoke(user, "what a nice day!");
			System.out.println(result);

			// private方法
			Method privateMethod = User.class.getDeclaredMethod("privateMethod");
			privateMethod.setAccessible(true);
			result = privateMethod.invoke(user);
			System.out.println(result);

			// protected方法
			Method protectedMethod = User.class.getDeclaredMethod("protectedMethod");
			result = protectedMethod.invoke(user);
			System.out.println(result);

			// package方法
			Method packageMethod = User.class.getDeclaredMethod("packageMethod");
			result = packageMethod.invoke(user);
			System.out.println(result);

			// 静态方法
			Method create = User.class.getMethod("create", String.class, int.class);
			User zhangsan = (User) create.invoke(null, "张三", 100);
			System.out.println(zhangsan);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void parameterApi() {
		System.out.println("==== parameterApi:");
		try {
			Method create = User.class.getMethod("create", String.class, int.class);
			Parameter[] parameters = create.getParameters();
			for (Parameter parameter : parameters) {
				String name = parameter.getName();
				if (parameter.isNamePresent()) {
					System.out.println("Name is present: " + name);
				} else {
					System.out.println("Name is not present: " + name);
				}
			}

			int parameterCount = create.getParameterCount();
			Class<?>[] parameterTypes = create.getParameterTypes();
			Annotation[][] parameterAnnotations = create.getParameterAnnotations();
			System.out.println(parameterCount);
			System.out.println(Arrays.toString(parameterTypes));
			System.out.println(Arrays.deepToString(parameterAnnotations));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	private void modifierApi() {
		System.out.println("==== modifierApi: ");

		System.out.println(Modifier.classModifiers());
		System.out.println(Modifier.methodModifiers());
		System.out.println(Modifier.parameterModifiers());
		System.out.println(Modifier.constructorModifiers());
		try {
			Field aNull = User.class.getField("NULL");
			int modifiers = aNull.getModifiers();
			System.out.println(Modifier.isPublic(modifiers));
			System.out.println(Modifier.isFinal(modifiers));
			System.out.println(Modifier.isStatic(modifiers));
			if (modifiers == (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL)) {
				System.out.println("public static final");
			}

			if (Modifier.isPublic(Modifier.PUBLIC) && Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
				System.out.println("public static final");
			}

			Method create = User.class.getMethod("create", String.class, int.class);
			modifiers = create.getModifiers();
			System.out.println(modifiers == (Modifier.PUBLIC | Modifier.STATIC));
			System.out.println(modifiers);
			modifiers = modifiers & Modifier.methodModifiers();
			System.out.println(modifiers);
			modifiers = modifiers | Modifier.methodModifiers();
			System.out.println(modifiers);
		} catch (NoSuchFieldException | NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
}