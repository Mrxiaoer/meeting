package com.meeting.modules.summary.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 每日工作清单entity
 *
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2019-01-21
 */
@TableName("met_daily_task")
public class DailyTaskEntity implements Serializable {

  /**
   * 主键
   */
  @TableId
  private Integer id;
  /**
   * 用户id
   */
  private Integer userId;
  /**
   * 标题
   */
  private String title;
  /**
   * 时间
   */
  private String date;
  /**
   * 昨日工作情况
   */
  private String yestDaily;
  /**
   * 今日工作情况
   */
  private String todDaily;
  /**
   * 创建时间
   */
  private LocalDateTime createTime;
  /**
   * 更新时间
   */
  private LocalDateTime modifiedTime;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getYestDaily() {
    return yestDaily;
  }

  public void setYestDaily(String yestDaily) {
    this.yestDaily = yestDaily;
  }

  public String getTodDaily() {
    return todDaily;
  }

  public void setTodDaily(String todDaily) {
    this.todDaily = todDaily;
  }

  public LocalDateTime getCreateTime() {
    return createTime;
  }

  public void setCreateTime(LocalDateTime createTime) {
    this.createTime = createTime;
  }

  public LocalDateTime getModifiedTime() {
    return modifiedTime;
  }

  public void setModifiedTime(LocalDateTime modifiedTime) {
    this.modifiedTime = modifiedTime;
  }
}
