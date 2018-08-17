package com.spider.modules.spider.utils;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import java.io.File;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-08-17
 */
public class FileIOUtil {

	private static Logger logger = LoggerFactory.getLogger(FileIOUtil.class);

	/**
	 * 将字符串写入文件
	 */
	public static void WriteStringToFile(String filePath, String str, boolean isAppend) throws IOException {
		File file = new File(filePath);
		//判断目标文件所在的目录是否存在
		if (!file.getParentFile().exists()) {
			//如果目标文件所在的目录不存在，则创建父目录
			if (!file.getParentFile().mkdirs()) {
				logger.error("文件夹{}创建失败", file.getParentFile());
				throw new IOException("文件夹创建失败");
			}
		}
		if (!file.exists()) {
			if (!file.createNewFile()) {
				logger.error("文件{}创建失败", filePath);
				throw new IOException("文件创建失败");
			}
		}
		//		try (FileWriter fw = new FileWriter(file, isAppend)) {
		//			try (BufferedWriter bw = new BufferedWriter(fw)) {
		//				bw.append(str);
		//				bw.flush();
		//			}
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}
		FileWriter writer = new FileWriter(filePath);
		if (isAppend) {
			writer.append(str);
		} else {
			writer.write(str);
		}
	}

	public static String readStringFromFile(String filePath) throws IOException {
		FileReader fileReader = new FileReader(filePath);
		return fileReader.readString();
	}

}
