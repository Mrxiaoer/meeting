package com.spider.modules.oss.cloud;

import com.spider.common.utils.ConfigConstant;
import com.spider.common.utils.Constant;
import com.spider.common.utils.SpringContextUtils;
import com.spider.modules.sys.service.SysConfigService;

/**
 * 文件上传Factory
 * 
 * @author maoxinmin
 */
public final class OSSFactory {
	private static SysConfigService sysConfigService;

	static {
		OSSFactory.sysConfigService = (SysConfigService) SpringContextUtils.getBean("sysConfigService");
	}

	public static CloudStorageService build() {
		// 获取云存储配置信息
		CloudStorageConfig config = sysConfigService.getConfigObject(ConfigConstant.CLOUD_STORAGE_CONFIG_KEY,
				CloudStorageConfig.class);

		if (config.getType() == Constant.CloudService.ALIYUN.getValue()) {
			return new AliyunCloudStorageService(config);
		}

		return null;
	}

}
