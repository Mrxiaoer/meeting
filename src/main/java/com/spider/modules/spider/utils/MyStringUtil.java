package com.spider.modules.spider.utils;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spider.modules.spider.entity.MyCookie;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StringUtil
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-28
 */
public class MyStringUtil {

	private static Logger logger = LoggerFactory.getLogger(MyStringUtil.class);

	//获取中文拼音首字母
	public static String getPinYinHeadChar(String str) {
		StringBuilder convert = new StringBuilder();
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert.append(pinyinArray[0].charAt(0));
			} else {
				convert.append(word);
			}
		}
		return convert.toString();
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
		StringBuilder str = new StringBuilder("[");
		int i = 0;
		for (Cookie cookie : cookies) {
			if (i > 0) {
				str.append(",");
			}
			str.append(JSONUtil.parse(cookie));
			i++;
		}
		str.append("]");
		return str.toString();
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
			cookies.add(new Cookie.Builder(myCookie.getName(), myCookie.getValue()).domain(myCookie.getDomain())
					.path(myCookie.getPath())
					.expiresOn(myCookie.getExpiry()).isHttpOnly(myCookie.isHttpOnly()).isSecure(myCookie.isSecure())
					.build());
		}
		return cookies;
	}

	public static String urlCutParam(String url) {
		Pattern p = Pattern.compile("(https?://[\\S]*?)[^A-Z|a-z|0-9|\\u4e00-\\u9fa5|.|/|:|_|-]");
		Matcher m = p.matcher(url);
		String newUrl;
		if (m.find()) {
			newUrl = m.group(1);
		} else {
			newUrl = url;
		}
		return newUrl;
	}

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
		try (FileWriter fw = new FileWriter(file, isAppend)) {
			try (BufferedWriter bw = new BufferedWriter(fw)) {
				bw.append(str);
				bw.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static StringBuilder readInfoFromFile(String filePath) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			return null;
		}
		StringBuilder resultStr = new StringBuilder();
		try (FileReader fr = new FileReader(file)) {
			try (BufferedReader bufferedReader = new BufferedReader(fr)) {
				String str;
				while (null != (str = bufferedReader.readLine())) {
					resultStr.append(str);
				}
			}
		}
		return resultStr;
	}

}
