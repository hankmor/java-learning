package com.belonk.collection.ref;

import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by sun on 2021/12/7.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class WeakHashMapDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * WeakHashMap用来保存WeakReference，使得规范映射更易于使用。 每个值仅一份已节约空间，允许垃圾回收自动清理键和值。
	 */

	public static void main(String[] args) throws InterruptedException {
		int size = 1000;
		// 用于保存强引用关系
		Key[] keys = new Key[size];
		WeakHashMap<Key, Value> map = new WeakHashMap<>();
		for (int i = 0; i < size; i++) {
			Key key = new Key(i + "");
			Value val = new Value(i + "");
			if (i % 3 == 0) {
				keys[i] = key; // 保存强引用关系
			}
			map.put(key, val);
		}

		// 触发GC
		System.gc();
		// 充分运行
		TimeUnit.SECONDS.sleep(1);
		/*
		 * 可以看到，被强引用的key不会被回收，其他的key都被回收了
		 */
	}
}

class Element {
	private String id;

	public Element(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		// 按照id比较
		if (o == null || getClass() != o.getClass()) return false;
		Element element = (Element) o;
		return Objects.equals(id, element.id);
	}

	@Override
	public int hashCode() {
		// 按照id生成hashcode
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	protected void finalize() {
		// 回收时调用
		System.out.println("Finalizing " + getClass().getSimpleName() + " " + this.id);
	}
}

class Key extends Element {
	public Key(String id) {
		super(id);
	}
}

class Value extends Element {
	public Value(String id) {
		super(id);
	}
}