package com.spider.modules.job.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.spider.common.utils.Constant;
import com.spider.common.utils.PageUtils;
import com.spider.common.utils.Query;
import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.business.model.TimeTaskModel;
import com.spider.modules.business.service.LinkInfoService;
import com.spider.modules.job.dao.ScheduleJobDao;
import com.spider.modules.job.entity.ScheduleJobEntity;
import com.spider.modules.job.model.ScheduleJobModel;
import com.spider.modules.job.service.ScheduleJobService;
import com.spider.modules.job.utils.ScheduleUtils;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

@Service("scheduleJobService")
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobDao, ScheduleJobEntity>
		implements ScheduleJobService {
	@Autowired
	private Scheduler scheduler;

	@Autowired
	LinkInfoService linkInfoService;

	/**
	 * 项目启动时，初始化定时器
	 */
	@PostConstruct
	public void init() {
		List<ScheduleJobEntity> scheduleJobList = this.selectList(null);
		for (ScheduleJobEntity scheduleJob : scheduleJobList) {
			CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
			// 如果不存在，则创建
			if (cronTrigger == null) {
				ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
			} else {
				ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
			}
		}
	}

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String beanName = (String) params.get("beanName");

		Page<ScheduleJobEntity> page = this.selectPage(new Query<ScheduleJobEntity>(params).getPage(),
				new EntityWrapper<ScheduleJobEntity>().like(StringUtils.isNotBlank(beanName), "bean_name", beanName));

		return new PageUtils(page);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(ScheduleJobEntity scheduleJob) {
		scheduleJob.setCreateTime(new Date());
		scheduleJob.setStatus(Constant.ScheduleStatus.NORMAL.getValue());
		this.insert(scheduleJob);

		ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update(ScheduleJobEntity scheduleJob) {
		ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);

		this.updateById(scheduleJob);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBatch(Long[] jobIds) {
		for (Long jobId : jobIds) {
			ScheduleUtils.deleteScheduleJob(scheduler, jobId);
		}

		// 删除数据
		this.deleteBatchIds(Arrays.asList(jobIds));
	}

	@Override
	public int updateBatch(Long[] jobIds, int status) {
		Map<String, Object> map = new HashMap<>();
		map.put("list", jobIds);
		map.put("status", status);
		return baseMapper.updateBatch(map);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void run(Long[] jobIds) {
		for (Long jobId : jobIds) {
			ScheduleUtils.run(scheduler, this.selectById(jobId));
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void pause(Long[] jobIds) {
		for (Long jobId : jobIds) {
			ScheduleUtils.pauseJob(scheduler, jobId);
		}

		updateBatch(jobIds, Constant.ScheduleStatus.PAUSE.getValue());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void resume(Long[] jobIds) {
		for (Long jobId : jobIds) {
			ScheduleUtils.resumeJob(scheduler, jobId);
		}

		updateBatch(jobIds, Constant.ScheduleStatus.NORMAL.getValue());
	}

	@Override
	public ScheduleJobModel challenge(ScheduleJobEntity scheduleJob){
		ScheduleJobModel model = new ScheduleJobModel();
		model.setBeanName(scheduleJob.getBeanName());
		model.setMethodName(scheduleJob.getMethodName());
		model.setCronExpression(scheduleJob.getCronExpression());
		model.setJobId(scheduleJob.getJobId());
		model.setCreateTime(scheduleJob.getCreateTime());
		model.setRemark(scheduleJob.getRemark());
		model.setStatus(scheduleJob.getStatus());
		String isNum = "^[0-9]*$";
		if(scheduleJob.getParams().matches(isNum)){
			LinkInfoEntity linkInfo = linkInfoService.queryById(Integer.parseInt(scheduleJob.getParams()));

			List<TimeTaskModel> timeTaskModels = new ArrayList<TimeTaskModel>();
			Map<String,Integer> flagMap = new HashMap<String,Integer>();

//			for(LinkInfoEntity entity : linkInfo){
//				if(!flagMap.containsKey(entity.getSystem())){
//					TimeTaskModel parent = new TimeTaskModel();
//					parent.setValue(entity.getSystem());
//					parent.setLabel(entity.getSystem());
//					List<TimeTaskModel> children = new ArrayList<TimeTaskModel>();
//					parent.setChildren(children);
//					flagMap.put(entity.getSystem(),timeTaskModels.size());
//					timeTaskModels.add(parent);
//				}
//
//				TimeTaskModel child = new TimeTaskModel();
//				child.setLabel(entity.getModule());
//				child.setValue(String.valueOf(entity.getLinkId()));
//				timeTaskModels.get(flagMap.get(entity.getSystem())).getChildren().add(child);
//			}
		}
		return  model;
	}
}
