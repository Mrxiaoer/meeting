package com.spider.modules.spider.pipeline;

import com.spider.modules.spider.dao.AnalogLoginDao;
import com.spider.modules.spider.entity.AnalogLoginEntity;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 检验登录(弃用)
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-05
 */
@Component
public class SpiderCheckLoginPipeline implements Pipeline {

	@Autowired
	private AnalogLoginDao analogLoginDao;

	@Override
	public void process(ResultItems resultItems, Task task) {
		AnalogLoginEntity siteInfo = new AnalogLoginEntity();
		List<String> selectObjs = resultItems.get("selectObjs");
		String str = null;
		if (selectObjs == null || selectObjs.toString().equals("")) {
			str = "- nothing -";
		} else {
			str = resultItems.get("selectObjs").toString();
		}
		siteInfo.setStrOfIndex(str + "<==>" + new Date());
		siteInfo.setHost(resultItems.get("host"));

		analogLoginDao.updateAnalogLogin(siteInfo);
	}

}
