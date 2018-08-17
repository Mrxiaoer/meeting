package com.spider.modules.spider.pipeline;

import com.spider.modules.spider.config.SpiderConstant;
import com.spider.modules.spider.entity.TemporaryRecordEntity;
import com.spider.modules.spider.service.TemporaryRecordService;
import com.spider.modules.spider.utils.FilePathUtil;
import com.spider.modules.spider.utils.MyStringUtil;
import java.io.IOException;
import java.util.List;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 页面临时信息
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-07
 */
@Component
public class SpiderTemporaryRecordPipeline implements Pipeline {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TemporaryRecordService temporaryRecordService;

	@Override
	public void process(ResultItems resultItems, Task task) {
		TemporaryRecordEntity temporaryRecord = new TemporaryRecordEntity();
		temporaryRecord.setUrl(resultItems.get(MyStringUtil.urlCutParam(SpiderConstant.URL)));
		List<String> objs = resultItems.get(SpiderConstant.SELECTOBJS);
		if (resultItems.get(SpiderConstant.SELECTOBJS) != null && objs.size() >= 1) {
			String html = objs.get(0);
			int linkId = resultItems.get(SpiderConstant.LINKID);
			//html写入文件
			String fileName = linkId + temporaryRecord.getUrl();
			String childPath = "static/htmlFile/" + DigestUtils.md5Hex(fileName) + ".html";
			logger.info("写入文件:{}", childPath);
			String filePath = FilePathUtil.getBasePath() +"/"+ childPath;
			System.out.println(filePath);
			try {
				MyStringUtil.WriteStringToFile(filePath, html, false);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("IOException:{}", e.getMessage());
			}
			temporaryRecord.setHtmlFilePath(childPath);
			temporaryRecord.setLinkId(linkId);
		}

		temporaryRecordService.saveOne(temporaryRecord);
	}

}
