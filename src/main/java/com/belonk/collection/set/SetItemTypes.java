package com.belonk.collection.set;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * 测试各种set与其中的元素的equals和hashcode方法以及实现实现Comparable接口的必须性。
 * <pre>
 * 结论:
 * Set中的元素都必须实现equals方法以保证元素的唯一性，另外：
 * 1、HashSet：元素必须实现hashcode
 * 2、TreeSet：元素必须实现Comparable接口
 * 3、LinkedHashSet：元素必须实现hashcode
 * </pre>
 * <p>
 * Created by sun on 2021/11/27.
 *
 * @author sunfuchang03@126.com
 * @since 3.0
 */
public class SetItemTypes {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	// 向set中填充元素，以便检查填充后元素的正确性
	static <T> void fill(Set<T> set, Class<T> item) {
		for (int i = 0; i < 10; i++) {
			try {
				set.add(item.getConstructor(int.class).newInstance(i));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	static <T> void test(Set<T> set, Class<T> item) {
		// 重复添加，测试元素的唯一性
		fill(set, item);
		fill(set, item);
		fill(set, item);
		System.out.println(set);
	}

	public static void main(String[] args) {
		// 正确的结果
		test(new HashSet<>(), HashType.class); // 不保证顺序，这里看起来是有序的
		test(new LinkedHashSet<>(), HashType.class);
		test(new TreeSet<>(), TreeType.class);
		// 不正确的结果：
		test(new HashSet<>(), SetType.class); // 未实现hashcode，导致元素重复
		test(new HashSet<>(), TreeType.class); // 未实现hashcode，导致元素重复
		test(new LinkedHashSet<>(), SetType.class); // 未实现hashcode，导致元素重复
		test(new LinkedHashSet<>(), TreeType.class); // 未实现hashcode，导致元素重复
		try {
			test(new TreeSet<>(), SetType.class); // 未实现Comparable：SetType cannot be cast to java.lang.Comparable
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			test(new TreeSet<>(), HashType.class); // 未实现Comparable：HashType cannot be cast to java.lang.Comparable
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		/*
		[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
		[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
		[9, 8, 7, 6, 5, 4, 3, 2, 1, 0]
		[3, 1, 8, 0, 9, 8, 7, 6, 6, 9, 1, 5, 3, 0, 7, 3, 8, 6, 1, 5, 2, 4, 5, 4, 2, 4, 9, 2, 7, 0]
		[1, 8, 8, 7, 5, 2, 2, 4, 0, 5, 9, 6, 0, 4, 7, 1, 1, 9, 7, 3, 4, 6, 3, 6, 5, 3, 8, 2, 9, 0]
		[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
		[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
		java.lang.ClassCastException: com.belonk.collection.set.SetType cannot be cast to java.lang.Comparable
		java.lang.ClassCastException: com.belonk.collection.set.HashType cannot be cast to java.lang.Comparable
		 */
	}
}

// 实现了equals方法，但是未实现hashcode方法
class SetType {
	int i;

	public SetType(int i) {
		this.i = i;
	}

	@Override
	public boolean equals(Object obj) {
		// 按照i的值来判断
		return obj instanceof SetType && ((SetType) obj).i == this.i;
	}

	@Override
	public String toString() {
		return Integer.toString(i);
	}
}

// Hash元素
class HashType extends SetType {
	public HashType(int i) {
		super(i);
	}

	@Override
	public int hashCode() {
		// 将i的值作为hashcode
		return i;
	}
}

// 树元素
class TreeType extends SetType implements Comparable<SetType> {
	public TreeType(int i) {
		super(i);
	}

	@Override
	public int compareTo(SetType o) {
		// 降序排列
		return Integer.compare(o.i, this.i);
	}
}