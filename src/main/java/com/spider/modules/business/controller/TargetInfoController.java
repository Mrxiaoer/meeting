package com.spider.modules.business.controller;

import com.spider.common.utils.R;
import com.spider.modules.business.entity.LinkInfoEntity;
import com.spider.modules.business.model.TargetInfoEntity;
import com.spider.modules.business.service.LinkInfoService;
import com.spider.modules.business.service.TargetInfoService;
import com.spider.modules.spider.entity.TemporaryRecordEntity;
import com.spider.modules.spider.service.AnalogLoginService;
import com.spider.modules.spider.service.TemporaryRecordService;
import com.spider.modules.spider.utils.FileIOUtil;
import org.openqa.selenium.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 采集页
 *
 * @author yaonuan
 * @data 2018年7月20日
 * version 1.0
 */
@RestController
@RequestMapping("/api/target")
public class TargetInfoController {

	@Autowired
	LinkInfoService linkInfoService;

	@Autowired
	AnalogLoginService analogLoginService;

	@Autowired
	TemporaryRecordService temporaryRecordService;

	@Autowired
	TargetInfoService targetInfoService;


	/**
	 * 采集登录页的Html
	 */
	@GetMapping("/analog_login_one")
	public R tospider(@RequestParam("id") Integer linkId) {
		try {
			String contents = FileIOUtil.readStringFromFile(targetInfoService.tospider(linkId));
			return R.ok().put("contents", contents);
		} catch (Exception e) {
			return R.error();
		}
	}

	/**
	 * 模拟登录
	 */
	@PostMapping("/analog_login_two")
	public R update(@RequestBody TargetInfoEntity targetInfo) {
		LinkInfoEntity linkInfoone = linkInfoService.queryById(targetInfo.getLinkId());
		Integer analogId = linkInfoone.getAnalogId();
		targetInfo.setUrl(linkInfoone.getUrl());
		LinkInfoEntity linkInfo;
		try {
			linkInfo = targetInfoService.update(targetInfo, analogId);
		} catch (Exception e) {
			return R.error("模拟登录失败~");
		}
		if (linkInfo == null) {
			return R.error("模拟登录失败~");
		} else if (linkInfo.getHasTarget() == 1){
			return R.ok();
		}else  {
			return R.error("模拟登录失败~");
		}
	}

	/**
	 * 单点采集采集页面，返回
	 */
	@PostMapping("/spdier_point")
	public R getOneById(@RequestBody Map<String,Object> params) {
		TemporaryRecordEntity temporary = null;
		try {
			temporary = targetInfoService.tothirdspider(params);
		}catch(NoSuchElementException nse){
			return R.error(1404, "某些页面元素未找到，可能页面被改动，请重新模拟登陆!");
		}catch (Exception e) {
			return R.error("获取页面异常，请重试或联系管理员!");
		}
		if (temporary == null) {
			return R.error("无法获取此页面，请查证后再拨!");
		}
		try {
			String contents = FileIOUtil.readStringFromFile(temporary.getHtmlFilePath());
			return R.ok().put("contents", contents);
		} catch (NoSuchElementException nse) {
			return R.error(1404, "某些页面元素未找到，可能页面被改动，请重新模拟登陆!");
		} catch (Exception e) {
			return R.error("无法获取此页面，请查看输入的cookie或用户名密码是否正确!");
		}

	}

	/**
	 * 前：表头xpath、linkId
	 *
	 * @return 采集到的表头信息list
	 */
	@PostMapping("/spider_head")
	public R getxpath(@RequestBody Map<String, Object> params) {
		Map<String, Object> map;
		map = targetInfoService.getXpath(params);
		return R.ok().put("pageInfos", map.get("pageInfos"));

	}

	/**
	 * 保存采集到的数据
	 */
	@PostMapping("/spider_head_update")
	public R updateHead(@RequestBody Map<String, Object> params) {
		targetInfoService.updateHead(params);
		return R.ok();
	}

	@PostMapping("/test")
    public R test(@RequestBody Map<String,Object> params){
	    System.out.println(params);
	    return R.ok();
    }
}
