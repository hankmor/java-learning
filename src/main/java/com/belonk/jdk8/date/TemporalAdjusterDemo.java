package com.belonk.jdk8.date;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * Created by sun on 2020/7/24.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class TemporalAdjusterDemo {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	public static void main(String[] args) {
		System.out.println(LocalDate.now());
		DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();

		// 下一个星期几的日期
		LocalDate with = LocalDate.now().with(TemporalAdjusters.next(dayOfWeek));
		System.out.println(with);

		// 今天
		with = LocalDate.now().with(TemporalAdjusters.nextOrSame(dayOfWeek));
		System.out.println(with);
	}
}