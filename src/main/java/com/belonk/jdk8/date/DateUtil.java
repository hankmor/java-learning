package com.belonk.jdk8.date;

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


    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //~ Instance fields ================================================================================================


    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================

    public static String dateToStr(Date date) {
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(dateTimeFormatter);
    }

    public static Date strToDate(String datetimeStr) {
        LocalDateTime localDateTime = LocalDateTime.parse(datetimeStr, dateTimeFormatter);
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        return Date.from(instant);
    }
}
