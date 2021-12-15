package com.belonk.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sun on 2018/3/13.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class DataMapQuartz {
	/*
	 * =================================================================================================================
	 *
	 * Static fields/constants
	 *
	 * =================================================================================================================
	 */

	private static Logger log = LoggerFactory.getLogger(DataMapQuartz.class);

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

		scheduler.start();

		Data data = new Data();
		data.setName("dt");
		JobDataMap map = new JobDataMap();
		map.put("data", data);
		String groupName = "group1";
		JobDetail jobDetail = JobBuilder.newJob(DumbJob.class).withIdentity("dumbJob", groupName)
				// 绑定数据
				.usingJobData("strValue", "this is a string.")
				.usingJobData("floatValue", 6.66f)
				.usingJobData("intValue", 66)
				.usingJobData(map)
				.build();

		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("dumbTrigger", groupName)
				.startNow().withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
				.build();

		scheduler.scheduleJob(jobDetail, trigger);

		Thread.sleep(10 * 1000);
		scheduler.shutdown();
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

	public static class DumbJob implements Job {
		private String strValue;

		// 提供set方法，JobDetail会自动合并值
		public void setStrValue(String strValue) {
			this.strValue = strValue;
		}

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			JobKey key = context.getJobDetail().getKey();
			// JobKey : group1.dumbJob
			System.out.println("JobKey : " + key);

			JobDataMap dataMap = context.getJobDetail().getJobDataMap();
			Data data = (Data) dataMap.get("data");
			// Data : dt
			System.out.println("Data : " + data.getName());

			String str = dataMap.getString("strValue");
			Float f = dataMap.getFloat("floatValue");
			Integer integer = dataMap.getInt("intValue");

			// string : this is a string.; float : 6.660000; integer : 66;
			System.out.println(String.format("string : %s; float : %f; integer : %d;", str, f, integer));

			// 获取属性合并到当前Job对象实现
			dataMap = context.getMergedJobDataMap();
			str = dataMap.getString("strValue");
			f = dataMap.getFloat("floatValue");
			integer = dataMap.getInt("intValue");

			// string : this is a string.; float : 6.660000; integer : 66;
			System.out.println(String.format("string : %s; float : %f; integer : %d;", str, f, integer));

			str = this.strValue;
			// String from JobDetail : this is a string.
			System.out.println("String from JobDetail : " + str);
		}
	}

	public static class Data {
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
