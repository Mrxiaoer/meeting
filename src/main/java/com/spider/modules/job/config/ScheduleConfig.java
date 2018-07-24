package com.spider.modules.job.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 定时任务配置，配置任务调度器 使用项目数据源作为quartz数据源
 *
 * @author maoxinmin
 * 
 * @param dataSource
 *            数据源实例
 */
@Configuration
@EnableScheduling
public class ScheduleConfig {

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		// 设置数据源，使用与项目统一数据源
		factory.setDataSource(dataSource);

		// quartz参数
		Properties prop = new Properties();
		prop.put("org.quartz.scheduler.instanceName", "SpiderScheduler");
		prop.put("org.quartz.scheduler.instanceId", "AUTO");
		// 线程池配置
		prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
		prop.put("org.quartz.threadPool.threadCount", "20");
		prop.put("org.quartz.threadPool.threadPriority", "5");
		prop.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
		// JobStore配置
		prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
		prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
		// 集群配置
		prop.put("org.quartz.jobStore.isClustered", "true");
		prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000");
		prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");

		prop.put("org.quartz.jobStore.misfireThreshold", "12000");
		prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
		prop.put("org.quartz.jobStore.useProperties", "false");

		factory.setQuartzProperties(prop);
		factory.setSchedulerName("SpiderScheduler");

		// 设置上下文spring bean name
		factory.setApplicationContextSchedulerContextKey("applicationContext");

		// 可选，QuartzScheduler,启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
		factory.setOverwriteExistingJobs(true);
		// 延时5秒启动
		factory.setStartupDelay(5);
		// 设置自动启动，默认为true
		factory.setAutoStartup(true);

		return factory;
	}
}
