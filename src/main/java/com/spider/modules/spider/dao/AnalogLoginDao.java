package com.spider.modules.spider.dao;

import com.spider.modules.spider.entity.AnalogLoginEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-26
 */
@Mapper
public interface AnalogLoginDao {

	/**
	 * 插入
	 *
	 * @param analogLogin
	 * @return
	 */
	int insertAnalogLogin(AnalogLoginEntity analogLogin);

	/**
	 * 更新
	 *
	 * @param analogLogin
	 * @return
	 */
	int updateAnalogLogin(AnalogLoginEntity analogLogin);

	/**
	 * 查找
	 *
	 * @param analogLogin
	 * @return
	 */
	List<AnalogLoginEntity> queryAnalogLogin(AnalogLoginEntity analogLogin);

	/**
	 * 查找一个
	 *
	 * @param analogLogin
	 * @return
	 */
	AnalogLoginEntity queryAnalogLoginLimit1(AnalogLoginEntity analogLogin);

	/**
	 * 根据linkId查找一个
	 *
	 * @param analogLogin
	 * @return
	 */
	AnalogLoginEntity queryAnalogLoginByLinkId(int linkId);

}
