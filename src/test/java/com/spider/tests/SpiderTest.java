package com.spider.tests;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import com.spider.modules.spider.utils.MyStringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

/**
 * 测试
 * ------------------------------
 *
 * @Author : lolilijve
 * @Email : 1042703214@qq.com
 * @Date : 2018-06-20
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SpiderTest {

	@Value("${webmagic.selenuim_config}")
	private String selenuim_config;

	@Test
	public void tt() {
		CronUtil.setMatchSecond(true);
		CronUtil.schedule("1", "*/5 * * * *", new Task() {
			@Override
			public void execute() {
				System.out.println("----");
			}
		});
		CronUtil.schedule("2", "*/5 * * * *", new Task() {
			@Override
			@Async("asyncServiceExecutor")
			public void execute() {
				System.out.println("++++");
			}
		});
		CronUtil.start();
		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void xxx() {
		for (int i = 0; i < 9; i++) {
			ThreadUtil.execute(new job() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName() + " === run ---- job");
				}
			});
		}
	}

	@Test
	public void startSpider() {
		//		new SpiderLink().startSpider("https://github.com/code4craft",true);
		System.setProperty("selenuim_config", selenuim_config);
		System.out.println(System.getProperty("selenuim_config"));
		//new SpiderPage().startSpider("https://github.com/code4craft",true);
	}

	@Test
	public void one2two() {
		One one = new One();
		one.setId(1);
		one.setAge(1);
		one.setName("yi");
		one.setSex("man");
		Two1 two1 = new Two1();
		Two2 two2 = new Two2();
		BeanUtil.copyProperties(one, two1);
		BeanUtil.copyProperties(one, two2);
		System.out.println("two1 ==> " + two1.toString());
		System.out.println("two2 ==> " + two2.toString());
	}

	@Test
	public void toCookies() {
		String doc = "[{\"path\":\"/citysystemgz\",\"domain\":\"114.55.11.227\",\"name\":\"JSESSIONID\",\"isHttpOnly\":false,\"isSecure\":false,\"value\":\"8FA1E5BB280B72D2000CE210279CAD55\"},{\"path\":\"/citysystemgz\",\"domain\":\"114.55.11.227\",\"name\":\"rememberMe\",\"isHttpOnly\":false,\"isSecure\":false,\"expiry\":1563956740000,\"value\":\"PXqTAKaPZpYqjOUmKU0/iv4nZv+QLPeUhlJoUcW/7QUT6rlKDWRSAjM0C07a9wRee7x/q2/cZg4+t2R8K1a0zzhf6oh7UguQH0ZKAp0qOlt1guUPFd+7cvQvEFN0eeCdQTI8Dai5UgJfnE3zgmbB6Jo2m5r47Dtx3e8IOYFwssT4SFMtRjQ1mchVayD0ixXcRBSVSeI44tUwbx4i+6xc3YLuzo/p7eeIfgh74Snv+S9fbFSFR9L6IRZrJJoyJEff454WwkX6vdXrBp1oz2WTe9EmiN4gbpZV6Gk2dMqA6p5rZxWOy5bYzGUbDoYgmNXpupPAhYrsdCc3Q73ArT74C6CO8pFLR6tbLsMAuWANzE2hPVUTHWaHe3spKwYpI0DF+1SFHbgtlFI/5zgscjMtIjgSSlOEI+Jtu0VbZZVtuE/Zvb9KiKZgfR6vbS3IvrZU0BPPYN1RvIAcem/w/bqS5al/B7siwLVb4j1SlhAis4yHow1HsMygQmedY0fxs9IqD0tyL4mnxeASROgElGKgM3VNeeTD6qzreHQIS3taiCg=\"}]";
		Set<Cookie> cookies = MyStringUtil.json2cookie(doc);
		System.out.println(cookies);
	}

	private class job implements Runnable {

		@Override
		public void run() {

		}

	}

	class One {
		private int id;
		private String name;
		private String sex;
		private int age;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}

	class Two1 {
		private int id;
		private String name;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Two1{" + "id=" + id + ", name='" + name + '\'' + '}';
		}
	}

	class Two2 {
		private int id;
		private String sex;
		private int age;

		public String getSex() {
			return sex;
		}

		public void setSex(String sex) {
			this.sex = sex;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		@Override
		public String toString() {
			return "Two2{" + "id=" + id + ", sex='" + sex + '\'' + ", age=" + age + '}';
		}
	}

}
