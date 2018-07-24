package com.spider.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.spider.modules.sys.entity.SysCaptchaEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * 验证码
 *
 * @author maoxinmin
 */
@Mapper
public interface SysCaptchaDao extends BaseMapper<SysCaptchaEntity> {

}
