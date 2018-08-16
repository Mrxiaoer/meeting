package com.spider.modules.spider.service.impl;

import com.spider.modules.spider.dao.TemporaryRecordDao;
import com.spider.modules.spider.entity.TemporaryRecordEntity;
import com.spider.modules.spider.service.TemporaryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 临时记录实现类
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-07
 */
@Service
public class TemporaryRecordServiceImpl implements TemporaryRecordService {

	@Autowired
	private TemporaryRecordDao temporaryRecordDao;

	@Override
	public void saveOne(TemporaryRecordEntity temporaryRecord) {
		int updateNum = temporaryRecordDao.updateOne(temporaryRecord);
		if (updateNum < 1) {
			temporaryRecordDao.insertOne(temporaryRecord);
		}
	}

	@Override
	public TemporaryRecordEntity getOneById(int id) {
		TemporaryRecordEntity tre = new TemporaryRecordEntity();
		tre.setId(id);
		return temporaryRecordDao.selectOne(tre);
	}

	@Override
	public TemporaryRecordEntity getOneByUrl(String url) {
		TemporaryRecordEntity tre = new TemporaryRecordEntity();
		tre.setUrl(url);
		return temporaryRecordDao.selectOne(tre);
	}

	@Override
	public TemporaryRecordEntity queryBylinkId(Integer linkId) {
		return temporaryRecordDao.queryBylinkId(linkId);
	}

}
