package com.spider.common.utils;

/**
 * Redis所有Keys
 *
 * @author maoxinmin
 */
public class RedisKeys {

	public static String getSysConfigKey(String key) {
		return "sys:config:" + key;
	}
}
