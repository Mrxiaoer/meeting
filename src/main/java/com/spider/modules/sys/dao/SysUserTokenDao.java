package com.spider.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.spider.modules.sys.entity.SysUserTokenEntity;

import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Token
 * 
 * @author maoxinmin
 */
@Mapper
public interface SysUserTokenDao extends BaseMapper<SysUserTokenEntity> {

	SysUserTokenEntity queryByToken(String token);

}
