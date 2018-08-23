package com.spider.modules.spider.dao;

import com.spider.modules.spider.entity.AnalogLoginEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-26
 */
@Mapper
public interface AnalogLoginDao {

	/**
	 * 插入
	 */
	int insertAnalogLogin(AnalogLoginEntity analogLogin);

	/**
	 * 更新
	 */
	int updateAnalogLogin(AnalogLoginEntity analogLogin);

	/**
	 * 更新（4个xpath与验证码图片为空也更）
	 */
	int update(AnalogLoginEntity analogLogin);

	/**
	 * 更新handcookie
	 */
	int updateHandCookie(AnalogLoginEntity analogLogin);

	/**
	 * 查找
	 */
	List<AnalogLoginEntity> queryAnalogLogin(AnalogLoginEntity analogLogin);

	/**
	 * 查找一个
	 */
	AnalogLoginEntity queryAnalogLoginLimit1(AnalogLoginEntity analogLogin);

	/**
	 * 根据linkId查找一个
	 */
	AnalogLoginEntity queryAnalogLoginByLinkId(int linkId);

}
