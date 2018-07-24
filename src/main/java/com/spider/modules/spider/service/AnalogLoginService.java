package com.spider.modules.spider.service;

import com.spider.modules.spider.entity.AnalogLoginEntity;

/**
 * AnalogLoginService
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-26
 */
public interface AnalogLoginService {

	/**
	 * 保存site信息
	 *
	 * @param analogLogin
	 * @return int
	 */
	int saveAnalogLogin(AnalogLoginEntity analogLogin);

	/**
	 * 根据id查找一个
	 *
	 * @param id
	 * @return AnalogLoginEntity
	 */
	AnalogLoginEntity getOneById(int id);

	/**
	 * 根据id查找一个
	 *
	 * @param linkId
	 * @return AnalogLoginEntity
	 */
	AnalogLoginEntity getOneByLinkId(int linkId);
	
}
