package com.spider.modules.spider.service.impl;

import com.spider.modules.spider.dao.AnalogLoginDao;
import com.spider.modules.spider.entity.AnalogLoginEntity;
import com.spider.modules.spider.service.AnalogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 模拟登录信息操作实现类
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-29
 */
@Service
public class AnalogLoginServiceImpl implements AnalogLoginService {

	@Autowired
	private AnalogLoginDao analogLoginDao;

	@Override
	public int saveAnalogLogin(AnalogLoginEntity siteInfo) {
		if (siteInfo.getId() == null) {
			analogLoginDao.insertAnalogLogin(siteInfo);
		} else {
			analogLoginDao.updateAnalogLogin(siteInfo);
		}

		return siteInfo.getId();
	}

	@Override
	public AnalogLoginEntity getOneById(int id) {
		AnalogLoginEntity analogLoginEntity = new AnalogLoginEntity();
		analogLoginEntity.setId(id);
		return analogLoginDao.queryAnalogLoginLimit1(analogLoginEntity);
	}

	@Override
	public AnalogLoginEntity getOneByLinkId(int linkId) {
		return analogLoginDao.queryAnalogLoginByLinkId(linkId);
	}

}
