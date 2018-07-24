package com.spider.modules.job.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.spider.common.utils.PageUtils;
import com.spider.common.utils.R;
import com.spider.modules.job.entity.ScheduleJobLogEntity;
import com.spider.modules.job.service.ScheduleJobLogService;

import java.util.Map;

/**
 * 定时任务日志
 *
 * @author maoxinmin
 */
@RestController
@RequestMapping("/sys/scheduleLog")
public class ScheduleJobLogController {
	@Autowired
	private ScheduleJobLogService scheduleJobLogService;

	/**
	 * 定时任务日志列表
	 */
	@GetMapping("/list")
	//@RequiresPermissions("sys:schedule:log")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = scheduleJobLogService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 定时任务日志信息
	 */
	@GetMapping("/info/{logId}")
	public R info(@PathVariable("logId") Long logId) {
		ScheduleJobLogEntity log = scheduleJobLogService.selectById(logId);

		return R.ok().put("log", log);
	}
}
