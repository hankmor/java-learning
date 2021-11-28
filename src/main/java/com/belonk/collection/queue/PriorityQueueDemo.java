package com.belonk.collection.queue;

import java.util.PriorityQueue;

/**
 * Created by sun on 2021/11/28.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class PriorityQueueDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		ToDoList toDoList = new ToDoList();
		toDoList.add("Morning Meeting", 'A', 1);
		toDoList.add("Coding", 'C', 1);
		toDoList.add("Work Report", 'A', 2);
		toDoList.add("Visit Custom", 'B', 1);
		toDoList.add("Afternoon Rest", 'C', 2);

		// 期望的优先级顺序：A1、A2、B1、C1、C2
		while (!toDoList.isEmpty()) {
			System.out.println(toDoList.remove());
		}

		/*
		A1: Morning Meeting
		A2: Work Report
		B1: Visit Custom
		C1: Coding
		C2: Afternoon Rest
		 */
	}
}

// 待办列表，包含一个字符串和给定的两个排序元素
class ToDoList extends PriorityQueue<ToDoList.ToDoItem> {
	/**
	 * 待办项：通过实现comparable接口实现排序
	 */
	public static class ToDoItem implements Comparable<ToDoItem> {
		private final String content;
		private final char priamry;
		private final int secondary;

		public ToDoItem(String content, char primary, int secondary) {
			this.content = content;
			this.priamry = primary;
			this.secondary = secondary;
		}

		@Override
		public int compareTo(ToDoItem o) {
			// 按照primary和secondary大小排序
			if (this.priamry > o.priamry) {
				return 1;
			}
			if (this.priamry == o.priamry) {
				if (this.secondary > o.secondary) {
					return 1;
				}
				if (this.secondary < o.secondary) {
					return -1;
				}
				return 0;
			}
			return -1;
		}

		@Override
		public String toString() {
			return this.priamry + String.valueOf(this.secondary) + ": " + this.content;
		}
	}

	public void add(String content, char primary, int secondary) {
		super.add(new ToDoItem(content, primary, secondary));
	}
}