package com.spider.modules.job.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.spider.common.utils.Constant;
import com.spider.common.utils.PageUtils;
import com.spider.common.utils.Query;
import com.spider.modules.business.dao.LinkInfoDao;
import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.job.dao.ScheduleJobDao;
import com.spider.modules.job.entity.ScheduleJobEntity;
import com.spider.modules.job.model.ScheduleJobModel;
import com.spider.modules.job.service.ScheduleJobService;
import com.spider.modules.job.utils.ScheduleUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("scheduleJobService")
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobDao, ScheduleJobEntity>
    implements ScheduleJobService {
  @Autowired LinkInfoDao linkInfoDao;
  @Autowired private Scheduler scheduler;
  @Autowired ScheduleJobDao scheduleJobDao;

  /** 项目启动时，初始化定时器 */
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

    Page<ScheduleJobEntity> page =
        this.selectPage(
            new Query<ScheduleJobEntity>(params).getPage(),
            new EntityWrapper<ScheduleJobEntity>()
                .like(StringUtils.isNotBlank(beanName), "bean_name", beanName).eq("del_state", Constant.VALUE_ONE));

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
//    this.deleteBatchIds(Arrays.asList(jobIds));
    scheduleJobDao.deleteJobIds(jobIds);
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
  public ScheduleJobModel challenge(ScheduleJobEntity scheduleJob) {
    ScheduleJobModel model = new ScheduleJobModel();
    model.setBeanName(scheduleJob.getBeanName());
    model.setMethodName(scheduleJob.getMethodName());
    model.setCronExpression(scheduleJob.getCronExpression());
    model.setJobId(scheduleJob.getJobId());
    model.setCreateTime(scheduleJob.getCreateTime());
    model.setRemark(scheduleJob.getRemark());
    model.setStatus(scheduleJob.getStatus());
    // params 转换模块名/系统名称的形式输出
    String isNum = "^[0-9]*$";
    if (scheduleJob.getParams() != null && scheduleJob.getParams().matches(isNum)) {
      LinkInfoEntity linkInfo = linkInfoDao.queryById(Integer.parseInt(scheduleJob.getParams()));
      List<String> params = new ArrayList<>();
      params.add(linkInfo.getSystem());
      params.add(String.valueOf(linkInfo.getLinkId()));
      model.setParam(params);
    }
    return model;
  }

  @Override
  public PageUtils challengePage(PageUtils pageUtils){
    @SuppressWarnings("unchecked")
    List<ScheduleJobEntity> scheduleJobs = pageUtils.getList();
    List<ScheduleJobModel> list = new ArrayList<>();
    for (ScheduleJobEntity value:scheduleJobs){
      ScheduleJobModel scheduleJobModel = new ScheduleJobModel();
      BeanUtil.copyProperties(value, scheduleJobModel);
      LinkInfoEntity link = linkInfoDao.selectById(Integer.parseInt(value.getParams()));
      if (link != null){
        scheduleJobModel.setParamsName(link.getSystem() + '/' + link.getModule());
        list.add(scheduleJobModel);
      }
      LinkInfoEntity linkInfo = linkInfoDao.queryById(Integer.parseInt(value.getParams()));

    }
    pageUtils.getList().clear();
    pageUtils.setList(list);
    pageUtils.setTotalCount(pageUtils.getList().size());
    return pageUtils;
  }
}
