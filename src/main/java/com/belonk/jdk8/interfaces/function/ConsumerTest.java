package com.belonk.jdk8.interfaces.function;

import java.util.function.Consumer;

class Person {
	private String id;

	public Person() {
	}

	public Person(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id='" + id + '\'' +
				'}';
	}

	public String getId() {
		return id;
	}
}

/**
 * Consumer 接口表示执行在单个参数上的操作
 */
public class ConsumerTest {
	//~ Static fields/initializers =====================================================================================


	//~ Instance fields ================================================================================================


	//~ Constructors ===================================================================================================


	//~ Methods ========================================================================================================
	public static void main(String[] args) {
		Consumer<Person> personConsumer = p -> System.out.println("id : " + p);
		personConsumer.accept(new Person("abc"));
	}
}
