package com.belonk.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by sun on 2021/11/28.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class CollectionsAndAsList {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		List<Person> people = Arrays.asList(new Male(), new Female(), new Monster());

		// JDK7编译会失败，因为Boy、Father都是继承自Male，所以asList创建的默认是List<Male>
		// JDK8编译成功，泛型类型推导
		List<Person> males = Arrays.asList(new Boy(), new Father());
		List<Person> people1 = new ArrayList<>();
		Collections.addAll(people1, new Boy(), new Father());

		// JDK8可以类型推导，不需要指定泛型类型<Person>
		List<Person> males1 = Arrays.<Person>asList(new Boy(), new Father());
		List<Person> males2 = Arrays.asList(new Boy(), new Father());
	}
}

class Person {
}

class Male extends Person {
}

class Boy extends Male {
}

class Father extends Male {
}

class Female extends Person {

}

// 不男不女的怪物
class Monster extends Person {

}