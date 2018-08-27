package com.spider.modules.spider.processor;

import static java.util.regex.Pattern.compile;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpStatus;
import com.spider.modules.spider.config.SpiderConstant;
import com.spider.modules.spider.entity.MyPage;
import com.spider.modules.spider.entity.MySite;
import com.spider.modules.spider.entity.SpiderRule;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.UrlUtils;

/**
 * 页面处理器
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-24
 */
public class SpiderPageProcessor implements PageProcessor {

	private Logger logger = LoggerFactory.getLogger(SpiderPageProcessor.class);

	/**
	 * 抓取网站的相关配置，包括编码、抓取间隔、重试次数等
	 */
	private MySite site = MySite.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(5000);

	private int linkId = 0;

	/**
	 * 用来存储driver信息
	 */
	private PhantomJSDriver phantomJSDriver;
	/**
	 * 用来存储cookie信息
	 */
	private Set<Cookie> cookies;
	/**
	 * 是否全站
	 */
	private boolean allDomain;
	/**
	 * 爬取规则
	 */
	private SpiderRule spiderRule;

	public SpiderPageProcessor(boolean allDomain, SpiderRule spiderRule) {
		this.allDomain = allDomain;
		if (spiderRule != null) {
			this.spiderRule = spiderRule;
		} else {
			this.spiderRule = new SpiderRule();
		}
	}

	public int getLinkId() {
		return linkId;
	}

	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}

	public PhantomJSDriver getPhantomJSDriver() {
		return phantomJSDriver;
	}

	public void setPhantomJSDriver(PhantomJSDriver phantomJSDriver) {
		this.phantomJSDriver = phantomJSDriver;
	}

	public Set<Cookie> getCookies() {
		return cookies;
	}

	public void setCookies(Set<Cookie> cookies) {
		this.cookies = cookies;
	}

	public void addCookie(Cookie cookie) {
		this.cookies.add(cookie);
	}

	public boolean isAllDomain() {
		return allDomain;
	}

	public void setAllDomain(boolean allDomain) {
		this.allDomain = allDomain;
	}

	public SpiderRule getSpiderRule() {
		return spiderRule;
	}

	public void setSpiderRule(SpiderRule spiderRule) {
		this.spiderRule = spiderRule;
	}

	/**
	 * process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
	 */
	@Override
	public void process(Page page) {
		MyPage myPage = (MyPage) page;
		myPage.putField(SpiderConstant.LINKID, this.linkId);
		if (myPage.getRawText() != null) {
			long startTime = System.currentTimeMillis();
			String htmlStr = myPage.getHtml().get();
			//清除js
			Pattern p = Pattern.compile("<script[^>]*?src[\\s]*?=[\\s]*?(\"[\\S]*?\"|\'[\\S]*?\')></script>");
			Pattern p1 = Pattern.compile(
					"<script[^>]*?src[\\s]*?=[\\s]*?(\"[\\S]*?(vue|element-ui/index)(.min)?.js\"|\'[\\S]*?"
							+ "(vue|element-ui/index)(.min)?.js\')[^>]*?></script>");
			Matcher m = p.matcher(htmlStr);
			Matcher m1 = p1.matcher(htmlStr);
			List<String> list = new ArrayList<>();
			while (m1.find()) {
				list.add(m1.group(0));
			}
			while (m.find()) {
				boolean flag = true;
				for (String aList : list) {
					if (aList.equals(m.group(0))) {
						flag = false;
					}
				}
				if (flag) {
					htmlStr = htmlStr.replaceAll(m.group(0), "");
				}
			}
			htmlStr = htmlStr.replaceAll("(<script[^>]*?>[^>]*?[^>\\s]+?[^>]*?</script>)", "");
			//		htmlStr = htmlStr.replaceAll("(<script[\\s|\\S]*?>[\\s|\\S]*?</script>)", "");
			htmlStr = htmlStr.replaceAll("type[\\s]*?=[\\s]*?(\"submit\"|'submit')", "");
			//url补全
			htmlStr = this.urlComplate(UrlUtils.getHost(myPage.getUrl().toString()), myPage.getUrlPath(), htmlStr);
			Html html = Html.create(htmlStr);

			//定义如何抽取页面信息，并保存下来
			Selectable select;
			String xpath = this.spiderRule.getXpath();
			//为空赋个默认值（整个HTML）
			if (StrUtil.isEmpty(xpath)) {
				xpath = "/html";
			}
			if (this.spiderRule.getIsGetText()) {
				//获取此xpath下直接或间接的文本，并根据需要对其进行正则提取
				select = html.xpath(xpath + "/allText()");
				if (StrUtil.isNotEmpty(this.spiderRule.getRegex())) {
					select = select.regex(this.spiderRule.getRegex());
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
				myPage.putField(SpiderConstant.SELECTSTRS, selectStrs);
			} else {
				//获取此xpath对应的元素，并根据需要对其进行正则提取
				select = html.xpath(xpath);
				if (StrUtil.isNotEmpty(this.spiderRule.getRegex())) {
					select = select.regex(this.spiderRule.getRegex());
				}
				//根据规则置换
				Map<String, String> ruleMap = spiderRule.getReplacementMap();
				if (ruleMap != null && ruleMap.size() > 0) {
					for (Map.Entry<String, String> rep : ruleMap.entrySet()) {
						select = select.replace(rep.getKey(), rep.getValue());
					}
				}
				myPage.putField(SpiderConstant.SELECTOBJS, select.all());
			}
			myPage.putField(SpiderConstant.HOST, UrlUtils.getHost(myPage.getUrl().toString()));
			myPage.putField(SpiderConstant.URL, myPage.getUrl().toString());
			logger.info("处理页面耗时{}毫秒", System.currentTimeMillis() - startTime);

			if (myPage.getResultItems().get(SpiderConstant.SELECTOBJS) == null
					&& myPage.getResultItems().get(SpiderConstant.SELECTSTRS) == null) {
				//skip this myPage（略过pipeline的执行）
				myPage.setSkip(true);
			}

			//从页面发现后续的url地址来抓取
			if (this.allDomain) {
				String domain = site.getDomain();
				myPage.addTargetRequests(html.links().regex("(https?://" + domain + "/[\\w\\-\\/]*)").all());
			}
		}
	}

	@Override
	public Site getSite() {

		//将获取到的cookie信息添加到webmagic中
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				site.addCookie(cookie.getDomain(), cookie.getName(), cookie.getValue());
			}
		}

		site.setPhantomJSDriver(phantomJSDriver);

		return site;
	}

	//页面链接补全
	private String urlComplate(String host, String path, String htmlStr) {
		Pattern pattern = compile("((href|url|src|action)[\\s]*?=[\\s]*?\"([\\s|\\S]+?)\")");
		Matcher matcher = pattern.matcher(htmlStr);

		//对所有链接进行补全
		if (StrUtil.isBlank(path)) {
			path = "";
		}
		String fullPath;
		if (!path.endsWith("/")) {
			path += "/";
		}
		while (matcher.find()) {
			try {
				fullPath = matcher.group()
						.replace(matcher.group(3), URLUtil.complateUrl(URLUtil.complateUrl(host, path), matcher.group(3)));
			} catch (Exception e) {
				logger.info(e.getMessage());
				continue;
			}
			htmlStr = htmlStr.replace(matcher.group(), fullPath);
		}
		return htmlStr;
	}

	/**
	 * 我发现。。。这个方法没什么用。。。
	 */
	private String getUrlPath(String host, String xmm, String afterUrl, List<String> pathList) {
		String oldXmm = xmm;

		String tryUrl = URLUtil.complateUrl(URLUtil.complateUrl(host, xmm), afterUrl);

		//测试是否可连接
		boolean isOk = false;
		int connCounts = 0;
		int maxTry = 3;
		while (connCounts < maxTry) {
			try {
				URL url = new URL(tryUrl);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				int state = con.getResponseCode();
				if (state == HttpStatus.HTTP_OK) {
					logger.info("项目名{}，链接可用！", oldXmm);
					xmm = oldXmm;
					isOk = true;
					break;
				} else {
					logger.info("项目名{}，链接不可用！HttpStatus：{}", oldXmm, state);
					xmm = pathList.get(0) + oldXmm;
					connCounts++;
				}
			} catch (Exception e) {
				logger.info("项目名{}，链接不可用:{}", oldXmm, e.getMessage());
				xmm = pathList.get(0) + oldXmm;
				connCounts++;
			}
		}

		if (!isOk) {
			pathList.remove(0);
			xmm = getUrlPath(host, xmm, afterUrl, pathList);
		}

		return xmm;
	}

}
