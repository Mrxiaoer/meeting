package com.spider.modules.spider.utils;

import java.io.File;
import java.io.FileNotFoundException;
import org.springframework.util.ResourceUtils;

/**
 * 文件路径工具
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-08-17
 */
public class FilePathUtil {

	/**
	 * 获取根目录
	 */
	public static String getBasePath() {

		File path = null;
		try {
			path = new File(ResourceUtils.getURL("classpath:").getPath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		assert (path != null);
		if (!path.exists()) {
			path = new File("");
		}

		return path.getAbsolutePath();
	}

}
