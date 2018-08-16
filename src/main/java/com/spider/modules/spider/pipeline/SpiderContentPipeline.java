package com.spider.modules.spider.pipeline;

import com.spider.modules.spider.config.SpiderConstant;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 爬取的内容结果操作
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-24
 */
@Component
public class SpiderContentPipeline implements Pipeline {

	@Override
	public void process(ResultItems resultItems, Task task) {
		//@TODO
		String st = null;
		if (null != resultItems.get(SpiderConstant.SELECTSTRS)) {
			st = StringUtils.join((List) resultItems.get(SpiderConstant.SELECTSTRS), SpiderConstant.SEPARATOR);
		} else if (null != resultItems.get(SpiderConstant.SELECTOBJS)) {
			st = StringUtils.join((List) resultItems.get(SpiderConstant.SELECTOBJS), SpiderConstant.SEPARATOR);
		}

	}

}
