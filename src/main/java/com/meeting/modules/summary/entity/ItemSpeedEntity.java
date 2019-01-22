package com.meeting.modules.summary.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("met_item_speed")
public class ItemSpeedEntity implements Serializable {

    /**
     * 主键
     */
    private Integer id;
    /**
     * 项目名称
     */
    private String itemName;
    /**
     * 进度
     */
    private Integer planSpeed;
    /**
     * 开始时间
     */
    private LocalDateTime beginTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 主要负责人
     */
    private String itemLeader;
    /**
     * 成员
     */
    private String itemMember;
    /**
     * 说明
     */
    private String remark;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime modifiedTime;
    /**
     *  是否删除（0，未删除；1，已删除）
     */
    private Integer isDelete;


}
