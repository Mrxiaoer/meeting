package com.spider.modules.spider.utils;

import java.io.InputStream;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 读取配置
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-07-02
 */
@Component
public class ReadProp {

	@Value("${webmagic.selenuim_config}")
	private String selenuimConfig;

	public Properties readp() {
		//new 一个Properties对象
		Properties p = new Properties();
		try {
			InputStream is = this.getClass().getResourceAsStream(selenuimConfig);
			p.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//		Enumeration em = p.propertyNames();
		//		Map<String, String> map = new HashMap<>();
		//		//如果存在更多元素
		//		while (em.hasMoreElements()) {
		//			//返回Object对象转换为String
		//			String key = (String) em.nextElement();
		//			//通过键获得值
		//			String value = p.getProperty(key);
		//			map.put(key, value);
		//		}
		return p;
	}
}
