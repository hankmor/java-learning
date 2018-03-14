package com.belonk.quartz;

import com.belonk.jdk8.date.DateUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.HolidayCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by sun on 2018/3/14.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class CalendarTest {
    /*
     * =================================================================================================================
     *
     * Static fields/constants
     *
     * =================================================================================================================
     */

    private static Logger log = LoggerFactory.getLogger(CalendarTest.class);

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /*
     * =================================================================================================================
     *
     * Instance fields
     *
     * =================================================================================================================
     */



    /*
     * =================================================================================================================
     *
     * Constructors
     *
     * =================================================================================================================
     */



    /*
     * =================================================================================================================
     *
     * Public Methods
     *
     * =================================================================================================================
     */

    public static void main(String[] args) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 定义一个假期日历，排除不调调度的日期
        Date excludedDate1 = DateUtil.strToDate("2018-03-15 00:00:00");
        Date excludedDate2 = DateUtil.strToDate("2018-03-16 00:00:00");
        HolidayCalendar holidayCalendar = new HolidayCalendar();
        holidayCalendar.addExcludedDate(excludedDate1);
        holidayCalendar.addExcludedDate(excludedDate2);

        scheduler.addCalendar("myHolidays", holidayCalendar, false, false);

        JobDetail jobDetail1 = JobBuilder.newJob(MyJob.class)
                .withIdentity("job1", "group1")
                .build();

        Trigger trigger1 = TriggerBuilder.newTrigger()
                .withIdentity("trigger1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(9, 30)) // 每天的9:300调度
                .modifiedByCalendar("myHolidays") // 按照日历修改调度日期
                .build();

        scheduler.scheduleJob(jobDetail1, trigger1);

        scheduler.start();
    }
    
    /*
     * =================================================================================================================
     *
     * Private Methods
     *
     * =================================================================================================================
     */

    /*
     * =================================================================================================================
     *
     * Inner classes
     *
     * =================================================================================================================
     */
    public static class MyJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("myjob is executing...");
        }
    }
}
