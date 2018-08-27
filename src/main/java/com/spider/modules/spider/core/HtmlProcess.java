package com.spider.modules.spider.core;

import cn.hutool.core.util.StrUtil;
import com.spider.modules.spider.dao.TemporaryRecordDao;
import com.spider.modules.spider.entity.SpiderRule;
import com.spider.modules.spider.entity.TemporaryRecordEntity;
import com.spider.modules.spider.utils.FileIOUtil;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

/**
 * HTML页面处理
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-16
 */
@Service
public class HtmlProcess {

	@Autowired
	private TemporaryRecordDao temporaryRecordDao;

	public List<String> process(TemporaryRecordEntity temporaryRecord, SpiderRule spiderRule) {
		//读取html文件内容
		temporaryRecord = temporaryRecordDao.selectOne(temporaryRecord);
		String filePath = temporaryRecord.getHtmlFilePath();
		String htmlStr = null;
		try {
			htmlStr = FileIOUtil.readStringFromFile(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//转化成html
		Html html = Html.create(htmlStr);

		if (StrUtil.isEmpty(spiderRule.getXpath())) {
			spiderRule.setXpath("/html");
		}
		Selectable select;
		if (spiderRule.getIsGetText()) {
			//获取此xpath下直接或间接的文本，并根据需要对其进行正则提取
			select = html.xpath(spiderRule.getXpath() + "/allText()");
			if (StrUtil.isNotEmpty(spiderRule.getRegex())) {
				select = select.regex(spiderRule.getRegex());
			}
			//根据规则置换
			Map<String, String> ruleMap = spiderRule.getReplacementMap();
			if (ruleMap != null && ruleMap.size() > 0) {
				for (Map.Entry<String, String> rep : ruleMap.entrySet()) {
					select = select.replace(rep.getKey(), rep.getValue());
				}
			}
			String selectStrAll = select.get();
			List<String> selectStrs = null;
			if (selectStrAll.length() > 0) {
				selectStrs = Arrays.asList(selectStrAll.split(" "));
			}
			return selectStrs;
		} else {
			//获取此xpath对应的元素，并根据需要对其进行正则提取
			select = html.xpath(spiderRule.getXpath());
			if (StrUtil.isNotEmpty(spiderRule.getRegex())) {
				select = select.regex(spiderRule.getRegex());
			}
			//根据规则置换
			Map<String, String> ruleMap = spiderRule.getReplacementMap();
			if (ruleMap != null && ruleMap.size() > 0) {
				for (Map.Entry<String, String> rep : ruleMap.entrySet()) {
					select = select.replace(rep.getKey(), rep.getValue());
				}
			}
			return select.all();
		}
	}

	//执行点击操作，在此之前请先获取目标页面
	public void doclick(PhantomJSDriver driver, String xpath) {
		WebElement ele = driver.findElementByXPath(xpath);
		ele.click();
	}

}
