package com.belonk.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by sun on 2018/3/15.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class SimpleTriggerTest {
	/*
	 * =================================================================================================================
	 *
	 * Static fields/constants
	 *
	 * =================================================================================================================
	 */

	private static Logger log = LoggerFactory.getLogger(SimpleTriggerTest.class);

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

	public static void main(String[] args) throws SchedulerException, InterruptedException {
		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
		Date date = new Date();
		// 获取下一个最近的15秒
		Date startTime = DateBuilder.nextGivenSecondDate(date, 15);
		System.out.println(startTime);

		String group1 = "group1";
		String group2 = "group2";

		// job1：固定时间触发一次
		JobDetail job = newJob(SimpleJob.class).withIdentity("job1", group1).build();
		SimpleTrigger trigger = (SimpleTrigger) newTrigger().withIdentity("trigger1", group1)
				.startAt(startTime).build();

		Date dt = scheduler.scheduleJob(job, trigger);
		p(job, trigger, dt);

		// job2：固定时间触发一次
		job = newJob(SimpleJob.class).withIdentity("job2", group1).build();
		trigger = (SimpleTrigger) newTrigger().withIdentity("trigger2", group1).startAt(startTime).build();

		dt = scheduler.scheduleJob(job, trigger);
		p(job, trigger, dt);

		// job3：执行4次，固定时间触发，每隔10秒重复一次，共重复3次
		job = newJob(SimpleJob.class).withIdentity("job3", group1).build();
		trigger = newTrigger().withIdentity("trigger3", group1)
				.startAt(startTime).withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(3))
				.build();

		dt = scheduler.scheduleJob(job, trigger);
		p(job, trigger, dt);

		// job3：执行3次，被另一个调度器执行，每隔10秒重复一次，共重复2次
		trigger = newTrigger().withIdentity("trigger3", group2)
				.startAt(startTime).withSchedule(simpleSchedule().withIntervalInSeconds(10).withRepeatCount(2))
				.forJob(job).build();

		dt = scheduler.scheduleJob(trigger);
		p(job, trigger, dt);

		// job4：当前时间后的第2分钟执行
		job = newJob(SimpleJob.class).withIdentity("job4", group1).build();
		trigger = (SimpleTrigger) newTrigger().withIdentity("trigger4", group1)
				.startAt(DateBuilder.futureDate(2, DateBuilder.IntervalUnit.MINUTE)).build();

		dt = scheduler.scheduleJob(job, trigger);
		p(job, trigger, dt);

		// 开始调度任务
		scheduler.start();

		Thread.sleep(300 * 1000);

		// 关闭调度，等待所有任务执行完成
		scheduler.shutdown(true);
	}

	/*
	 * =================================================================================================================
	 *
	 * Private Methods
	 *
	 * =================================================================================================================
	 */

	private static void p(JobDetail job, SimpleTrigger trigger, Date executeDate) {
		JobKey jobKey = job.getKey();
		int repeatCnt = trigger.getRepeatCount();
		long interval = trigger.getRepeatInterval() / 1000;
		System.out.println(String.format("JobKey: %s, 执行时间: %s, 重复次数: %d, 重复间隔: %d", jobKey, executeDate, repeatCnt, interval));
	}

	/*
	 * =================================================================================================================
	 *
	 * Inner classes
	 *
	 * =================================================================================================================
	 */
	public static class SimpleJob implements Job {
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			JobKey jobKey = context.getJobDetail().getKey();
			TriggerKey triggerKey = context.getTrigger().getKey();
			System.out.println("JobKey : " + jobKey + ", TriggerKey ： " + triggerKey + ", 执行时间 : " + new Date());
		}
	}
}
