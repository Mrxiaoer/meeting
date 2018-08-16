package com.spider.modules.spider.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spider.modules.spider.utils.MyStringUtil;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.ibatis.type.Alias;

/**
 * 爬取规则
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-26
 */
@Alias("spiderRule")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "spider_rule")
public class SpiderRule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * xpath选择器
	 */
	@Column(columnDefinition = "varchar(255) COMMENT 'xpath选择器'")
	private String xpath;

	/**
	 * 正则表达式
	 */
	@Column(columnDefinition = "varchar(255) COMMENT '正则表达式'")
	private String regex;

	/**
	 * 置换规则
	 */
	@Column(columnDefinition = "varchar(255) default '{}' COMMENT '置换规则'")
	private String replacement = "{}";

	/**
	 * 是否为获取文本(默认为是)
	 */
	@Column(columnDefinition = "boolean default true COMMENT '是否为获取文本'")
	private boolean isGetText = true;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public Map<String, String> getReplacementMap() {
		return MyStringUtil.json2map(this.replacement);
	}

	public void setReplacementMap(Map<String, String> replacement) {
		this.replacement = MyStringUtil.map2json(replacement);
	}

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

	public boolean getIsGetText() {
		return isGetText;
	}

	public void setIsGetText(boolean isGetText) {
		this.isGetText = isGetText;
	}

}
