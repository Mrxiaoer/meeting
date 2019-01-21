package com.meeting.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.meeting.common.utils.R;
import com.meeting.modules.sys.entity.SysUserTokenEntity;

/**
 * 用户Token
 * 
 * @author maoxinmin
 */
public interface SysUserTokenService extends IService<SysUserTokenEntity> {

	/**
	 * 生成token
	 * 
	 * @param userId
	 *            用户ID
	 */
	R createToken(long userId);

	/**
	 * 退出，修改token值
	 * 
	 * @param userId
	 *            用户ID
	 */
	void logout(long userId);

}
