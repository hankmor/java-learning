package com.belonk.lang.reflect;

import java.util.Objects;

/**
 * Created by sun on 2021/11/8.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class User extends Id implements Talk {
	//~ Static fields/constants/initializer

	public static final User NULL = new User(null, 0);

	//~ Instance fields

	// 包访问
	double lat;
	// 子类访问
	protected float salary;
	private String name;
	private int age;

	//~ Constructors

	public User() {

	}

	private User(String name) {
		this.name = name;
	}

	protected User(int age) {
		this.age = age;
	}

	public User(String name, int age) {
		this.name = name;
		this.age = age;
	}

	//~ Methods

	public boolean isNull() {
		return "none".equals(this.getName());
	}

	public static User create(String name, int age) {
		return new User(name, age);
	}

	@Override
	public void talk(String content) {
		System.out.println(this.getName() + " is talking: " + content);
	}

	private void privateMethod() {
		System.out.println("this is a private method.");
	}

	protected void protectedMethod() {
		System.out.println("this is a protected method.");
	}

	void packageMethod() {
		System.out.println("this is a package method.");
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return age == user.age && Objects.equals(name, user.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, age);
	}

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}