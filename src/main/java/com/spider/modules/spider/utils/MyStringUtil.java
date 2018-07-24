package com.spider.modules.spider.utils;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spider.modules.spider.entity.MyCookie;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.openqa.selenium.Cookie;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-28
 */
public class MyStringUtil {

	//获取中文拼音首字母
	public static String getPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}

	public static Map<String, String> json2map(String json) {
		Map<String, String> map = new HashMap<>(4);
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (json != null) {
				map = mapper.readValue(json, new TypeReference<HashMap<String, String>>() {});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static String map2json(Map<String, String> map) {
		ObjectMapper mapper = new ObjectMapper();
		String json = "";
		try {
			json = mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static String cookie2json(Set<Cookie> cookies) {
		String str = "[";
		int i = 0;
		for (Cookie cookie : cookies) {
			if (i > 0) {
				str += ",";
			}
			str += JSONUtil.parse(cookie);
			i++;
		}
		str += "]";
		return str;
	}

	public static Set<Cookie> json2cookie(String jsons) {
		if (jsons.startsWith("[")) {
			jsons = jsons.substring(1, jsons.length() - 1);
		}
		if (jsons.endsWith("]")) {
			jsons = jsons.substring(0, jsons.length() - 2);
		}
		String[] js = jsons.split("},");
		Set<Cookie> cookies = new HashSet<>();
		for (String j : js) {
			if (!j.endsWith("}")) {
				j += "}";
			}

			MyCookie myCookie = JSONUtil.toBean(j, MyCookie.class);
			cookies.add(new Cookie.Builder(myCookie.getName(), myCookie.getValue()).domain(myCookie.getDomain()).path(myCookie.getPath())
					            .expiresOn(myCookie.getExpiry()).isHttpOnly(myCookie.isHttpOnly()).isSecure(myCookie.isSecure()).build());
		}
		return cookies;
	}

}
