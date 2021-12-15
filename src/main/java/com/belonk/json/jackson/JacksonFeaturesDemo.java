package com.belonk.json.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

/**
 * Created by sun on 2019/4/11.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class JacksonFeaturesDemo {
	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Static fields/constants/initializer
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */



	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Instance fields
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */



	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Constructors
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */



	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Public Methods
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */

	public static void main(String[] args) throws Exception {
		JacksonFeaturesDemo demo = new JacksonFeaturesDemo();
		demo.mapperFeature();
		System.out.println("-----------------");
		demo.serializationFeature();
		System.out.println("-----------------");
		demo.deserializationFeature();
	}

	public void mapperFeature() throws JsonProcessingException {
		User user = new User("张三", null, "read");
		// 使用前启用配置特性才有效
		ObjectMapper objectMapper = new ObjectMapper();
		// 启用按照字母顺序排序
		objectMapper.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
		String json = objectMapper.writeValueAsString(user);
		System.out.println(json);

		// 新创建一个mapper
		objectMapper = objectMapper.copy();
		// 禁用按照字母排序
		objectMapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
		json = objectMapper.writeValueAsString(user);
		System.out.println(json);
	}

	public void serializationFeature() throws JsonProcessingException {
		User user = new User("lisi", null, "read");
		// User user = new User("<p>ss<p>", null, "read");
		// User user = new User("function say(var name) {alert(name);}", null, "read");
		// 获取writer
		ObjectMapper objectMapper = new ObjectMapper();
		final ObjectWriter writer = objectMapper.writer();
		// 启用格式化输出
		String json = writer.with(SerializationFeature.INDENT_OUTPUT).writeValueAsString(user);
		System.out.println(json);
		// 禁用空bean（无getter、无注解或无序列化器）时抛出异常，输出{}
		Object emptyBean = new Object();
		System.out.println(writer.without(SerializationFeature.FAIL_ON_EMPTY_BEANS).writeValueAsString(emptyBean));

		// 以类名称作为json的属性存储对象
		json = writer.with(SerializationFeature.WRAP_ROOT_VALUE).writeValueAsString(user);
		System.out.println(json);
	}

	public void deserializationFeature() throws IOException {
		User user = new User("lisi", null, "read");
		// 获取writer
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(user);

		final ObjectReader reader = objectMapper.readerFor(User.class);
		// 启用格式化输出
		User user1 = reader.readValue(json);
		System.out.println(user1);

	}

	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Protected Methods
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */



	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Property accessors
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */



	/*
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 *
	 * Inner classes
	 *
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */

	// @JsonRootName("uu")
	public static class User {
		private String name;
		private Integer age;
		private String topHover;

		public User() {
		}

		public User(String name, Integer age, String topHover) {
			this.name = name;
			this.age = age;
			this.topHover = topHover;
		}

		// @JsonValue
		// @JsonRawValue
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public String getTopHover() {
			return topHover;
		}

		public void setTopHover(String topHover) {
			this.topHover = topHover;
		}

		@Override
		public String toString() {
			return "User{" +
					"name='" + name + '\'' +
					", age=" + age +
					", topHover='" + topHover + '\'' +
					'}';
		}
	}
}