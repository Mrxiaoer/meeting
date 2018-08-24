package com.spider.tests;

import com.spider.modules.spider.utils.FileIOUtil;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-08-16
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class FileTest {

	@Test
	public void fileWrite() throws IOException {
		String str = "hello world!";
		String str1 = "hello yujia!";
		FileIOUtil.WriteStringToFile("F:\\JavaSource\\workspeace\\2018_guomai\\spider-data\\src\\main\\resources\\static"
				+ "\\htmlFile\\file\\test.html", str, false);
	}

}
