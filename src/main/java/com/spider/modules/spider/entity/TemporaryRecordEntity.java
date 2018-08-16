package com.spider.modules.spider.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.ibatis.type.Alias;

/**
 * 临时记录
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-07
 */
@Alias("temporaryRecord")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "spider_temporary_record")
public class TemporaryRecordEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	//编号
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	//对应的LinkId
	@Column(columnDefinition = "int(11) default 0 COMMENT '对应的LinkId'", nullable = false)
	private int linkId;
	//目标链接
	@Column(columnDefinition = "varchar(255) default '' COMMENT '目标链接'", nullable = false)
	private String url;
	//HTML页面
	@Column(columnDefinition = "longtext COMMENT 'HTML页面文件地址'", nullable = false)
	private String htmlFilePath;
	//创建时间
	@Column(columnDefinition = "datetime default CURRENT_TIMESTAMP COMMENT '创建时间'", nullable = false)
	private Date createTime;
	//更新时间
	@Column(columnDefinition = "datetime default CURRENT_TIMESTAMP COMMENT '更新时间'", nullable = false)
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getLinkId() {
		return linkId;
	}

	public void setLinkId(int linkId) {
		this.linkId = linkId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHtmlFilePath() {
		return htmlFilePath;
	}

	public void setHtmlFilePath(String htmlFilePath) {
		this.htmlFilePath = htmlFilePath;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
