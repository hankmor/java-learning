package com.belonk.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sun on 2018/3/12.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class HelloQuartz {
    /*
     * =================================================================================================================
     *
     * Static fields/initializers
     *
     * =================================================================================================================
     */

    private static Logger log = LoggerFactory.getLogger(HelloQuartz.class);

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
        // 获取调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        // 开始调度器
        scheduler.start();

        // 定义任务
        String groupName = "group1";
        String name = "helloJob";
        /*
         * 构造任务时，需要一个Job接口的实现类。调度程序执行任务时，每次在调用execute(..)方法之前创建一个该类的新实例，
         * 并且该实例必须拥有无参构造器。
         */
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class).withIdentity(name, groupName).build();

        // 定义触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", groupName)
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
                .build();

        // 使用触发器开始调度任务
        scheduler.scheduleJob(jobDetail, trigger);

        // 关闭调度器
//        scheduler.shutdown();
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

    public static class HelloJob implements Job {
        // 定义属性name没有意义，无法传递其值
        private String name;

        // 没有无参构造器，初始化失败
        // public HelloJob(String name) {
        //     this.name = name;
        // }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Hello job！");
        }
    }
}