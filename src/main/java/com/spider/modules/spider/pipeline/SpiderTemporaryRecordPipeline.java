package com.spider.modules.spider.pipeline;

import com.spider.modules.spider.config.SpiderConstant;
import com.spider.modules.spider.entity.TemporaryRecordEntity;
import com.spider.modules.spider.service.TemporaryRecordService;
import com.spider.modules.spider.utils.MyStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

/**
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-07
 */
@Component
public class SpiderTemporaryRecordPipeline implements Pipeline {

	@Autowired
	private TemporaryRecordService temporaryRecordService;

	@Override
	public void process(ResultItems resultItems, Task task) {
		TemporaryRecordEntity temporaryRecord = new TemporaryRecordEntity();
		temporaryRecord.setUrl(resultItems.get(MyStringUtil.urlCutParam(SpiderConstant.URL)));
		if (resultItems.get(SpiderConstant.SELECTOBJS) != null && ((List<String>) resultItems.get(SpiderConstant.SELECTOBJS)).size() >= 1) {
			String html = ((List<String>) resultItems.get(SpiderConstant.SELECTOBJS)).get(0);
			int linkId = resultItems.get(SpiderConstant.LINKID);
			temporaryRecord.setHtml(html);
			temporaryRecord.setLinkId(linkId);
		}

		temporaryRecordService.saveOne(temporaryRecord);
	}

}
