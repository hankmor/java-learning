package com.belonk.collection.map;

import com.belonk.util.Printer;

import java.util.LinkedHashMap;

/**
 * Created by sun on 2021/12/1.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class LinkedHashMapDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		/*
		LinkedHashMap：散列存储元素，查询时按照元素添加的顺序返回，既保证了存储效率，也保证了顺序性。另外，LinkedHashMap可以通过构造
		器指定使用LRU(最少使用算法)将最少使用的元素排在最前（可以用于定期清理的场景）。
		 */
		LinkedHashMap<Integer, String> linkedHashMap = new LinkedHashMap<>(new AlphabeticMapData(10));
		Printer.println(linkedHashMap);

		// 构造使用LRU算法的map，accessOrder参数：true则最少访问的排在前边(最近访问的元素移动到最后)，false则按照插入顺序
		linkedHashMap = new LinkedHashMap<>(10, 0.75f, true);
		linkedHashMap.putAll(new AlphabeticMapData(10));
		// 访问前6个元素
		for (int i = 0; i < 6; i++) {
			linkedHashMap.get(i);
		}
		Printer.println(linkedHashMap);
		// 访问最后一个元素
		linkedHashMap.get(9);
		Printer.println(linkedHashMap);
		/*:
		{0=A, 1=B, 2=C, 3=D, 4=E, 5=F, 6=G, 7=H, 8=I, 9=J}
		{6=G, 7=H, 8=I, 9=J, 0=A, 1=B, 2=C, 3=D, 4=E, 5=F}
		{6=G, 7=H, 8=I, 0=A, 1=B, 2=C, 3=D, 4=E, 5=F, 9=J}
		 */
	}
}