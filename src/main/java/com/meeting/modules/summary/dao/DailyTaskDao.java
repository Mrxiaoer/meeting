package com.meeting.modules.summary.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.meeting.modules.summary.entity.DailyTaskEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DailyTaskDao extends BaseMapper<DailyTaskEntity> {
}
