package com.meeting.modules.sys.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.meeting.modules.sys.entity.SysUserTokenEntity;

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
