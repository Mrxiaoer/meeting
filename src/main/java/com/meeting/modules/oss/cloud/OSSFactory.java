package com.meeting.modules.oss.cloud;

import com.meeting.common.utils.ConfigConstant;
import com.meeting.common.utils.Constant;
import com.meeting.common.utils.SpringContextUtils;
import com.meeting.modules.sys.service.SysConfigService;

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
