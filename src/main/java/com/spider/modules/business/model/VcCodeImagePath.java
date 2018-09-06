package com.spider.modules.business.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 获取配置信息中图片路径映射
 * @Author : yaonuan
 * @Email : 806039077@qq.com
 * @Date : 2018-08-09
 */
@Component
@PropertySource({"classpath:application.yml"})
@ConfigurationProperties
public class VcCodeImagePath {

    @Value("${VcCode.image.path}")
    private String newPath;

    @Value("${VcCode.image.p-path}")
    private String oldPath;

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }
}
