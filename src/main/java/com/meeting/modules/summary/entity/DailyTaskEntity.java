package com.meeting.modules.summary.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 每日工作清单entity
 *
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2019-01-21
 */
@Data
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
  /**
   * 是否删除（0，未删除；1，已删除）
   */
  private Integer isDelete;

}
