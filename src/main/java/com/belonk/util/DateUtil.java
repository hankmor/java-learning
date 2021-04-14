package com.belonk.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by sun on 2018/2/9.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class DateUtil {
	//~ Static fields/initializers =====================================================================================


	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final DateTimeFormatter dateTimeMsFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
	private static final DateTimeFormatter timeMsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.S");
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	//~ Instance fields ================================================================================================


	//~ Constructors ===================================================================================================


	//~ Methods ========================================================================================================

	public static String date(Date date) {
		return format(date, dateFormatter);
	}

	public static String timeMs(Date date) {
		return format(date, timeMsFormatter);
	}

	public static String time(Date date) {
		return format(date, timeFormatter);
	}

	public static String dateTime(Date date) {
		return format(date, dateTimeFormatter);
	}

	public static String dateTimeMs(Date date) {
		return format(date, dateTimeMsFormatter);
	}

	public static String format(Date date, String format) {
		Instant instant = date.toInstant();
		LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		return dateTime.format(DateTimeFormatter.ofPattern(format));
	}

	private static String format(Date date, DateTimeFormatter formatter) {
		Instant instant = date.toInstant();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		return localDateTime.format(formatter);
	}

	public static Date parse(String datetimeStr) {
		LocalDateTime localDateTime = LocalDateTime.parse(datetimeStr, dateTimeFormatter);
		Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
		return Date.from(instant);
	}

	public static void main(String[] args) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		System.out.println(LocalDateTime.now().format(dateFormatter));

		System.out.println(DateUtil.date(new Date()));
		System.out.println(DateUtil.time(new Date()));
		System.out.println(DateUtil.timeMs(new Date()));
	}
}
