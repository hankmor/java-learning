package com.belonk.collection.map;

import com.belonk.util.Printer;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by sun on 2021/12/1.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class SortedMapDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		// 创建SortedMap，按照key自然顺序排序
		SortedMap<Integer, String> sortedMap = new TreeMap<>(new AlphabeticMapData(27));
		Printer.println(sortedMap);

		// 第一个key为最小值
		Integer low = sortedMap.firstKey();
		Integer high = sortedMap.lastKey();
		// 获取第10和第20个元素的key
		for (Integer integer : sortedMap.keySet()) {
			if (integer == 10) {
				low = integer;
			}
			if (integer == 20) {
				high = integer;
			}
		}

		Printer.println("low: " + low);
		Printer.println("high: " + high);

		// 第10到第20（不包含）个元素的子map
		SortedMap<Integer, String> subMap = sortedMap.subMap(low, high);
		Printer.println(subMap);
		// 子map只能修改范围内的元素，超过范围抛出异常：java.lang.IllegalArgumentException: key out of range
		subMap.put(subMap.firstKey(), subMap.firstKey() + subMap.get(subMap.firstKey()));
		// 超过范围
		// subMap.put(9, "less");
		// subMap.put(20, "great");
		Printer.println(subMap);
		SortedMap<Integer, String> headMap = sortedMap.headMap(low);
		Printer.println(headMap);
		headMap.put(headMap.firstKey(), headMap.firstKey() + headMap.get(headMap.firstKey()));
		Printer.println(headMap);
		// 超过范围
		// headMap.put(100, "100");
		SortedMap<Integer, String> tailMap = sortedMap.tailMap(high);
		Printer.println(tailMap);
		tailMap.put(tailMap.lastKey(), tailMap.lastKey() + tailMap.get(tailMap.lastKey()));
		Printer.println(tailMap);
		// 超过范围
		// tailMap.put(101, "101");

		sortedMap.put(999, "last");
		Printer.println(sortedMap);

		/*
		{0=A, 1=B, 2=C, 3=D, 4=E, 5=F, 6=G, 7=H, 8=I, 9=J, 10=K, 11=L, 12=M, 13=N, 14=O, 15=P, 16=Q, 17=R, 18=S, 19=T, 20=U, 21=V, 22=W, 23=X, 24=Y, 25=Z, 26=A}
		low: 10
		high: 20
		{10=K, 11=L, 12=M, 13=N, 14=O, 15=P, 16=Q, 17=R, 18=S, 19=T}
		{10=10K, 11=L, 12=M, 13=N, 14=O, 15=P, 16=Q, 17=R, 18=S, 19=T}
		{0=A, 1=B, 2=C, 3=D, 4=E, 5=F, 6=G, 7=H, 8=I, 9=J}
		{0=0A, 1=B, 2=C, 3=D, 4=E, 5=F, 6=G, 7=H, 8=I, 9=J}
		{20=U, 21=V, 22=W, 23=X, 24=Y, 25=Z, 26=A}
		{20=U, 21=V, 22=W, 23=X, 24=Y, 25=Z, 26=26A}
		{0=0A, 1=B, 2=C, 3=D, 4=E, 5=F, 6=G, 7=H, 8=I, 9=J, 10=10K, 11=L, 12=M, 13=N, 14=O, 15=P, 16=Q, 17=R, 18=S, 19=T, 20=U, 21=V, 22=W, 23=X, 24=Y, 25=Z, 26=26A, 999=last}
		 */
	}
}